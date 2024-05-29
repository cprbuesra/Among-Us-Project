package org.fhv.amongus.player.player.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.jwt.model.JwtToken;
import org.fhv.amongus.player.jwt.service.JwtTokenRepositoryService;
import org.fhv.amongus.player.jwt.service.JwtTokenService;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.model.Role;
import org.fhv.amongus.player.player.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepositoryService playerRepositoryService;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenRepositoryService jwtTokenRepositoryService;
    private final PlayerRepository playerRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);


    public AuthResponse savePlayer(AuthRequest authRequest) {
        Optional<Player> existingPlayer = playerRepositoryService.findByUsername(authRequest.getUsername());
        if (existingPlayer.isPresent()) {
            logger.warn("Player with Username: {} already exists with ID: {}", authRequest.getUsername(), existingPlayer.get().getPlayerId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player already exists");
        } else {
            logger.info("Saving player with Username: {}", authRequest.getUsername());
            var player = Player.builder()
                    .username(authRequest.getUsername())
                    .x(335)
                    .y(20)
                    .flip(false)
                    .status("ALIVE")
                    .build();
            playerRepositoryService.savePlayer(player);


            var jwtToken = jwtTokenService.generateToken(player);
            logger.info("Generated JWT Token: {}", jwtToken);
            var expiryDate = jwtTokenService.extractExpiration(jwtToken);
            String sessionId = UUID.randomUUID().toString();

            JwtToken jwtTokenObj = JwtToken.builder()
                    .token(jwtToken)
                    .player(player)
                    .expirationDate(expiryDate)
                    .sessionId(sessionId)
                    .build();

            jwtTokenRepositoryService.saveToken(jwtTokenObj);
            logger.info("Saved JWT Token: {}", jwtTokenObj);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .sessionId(sessionId)
                    .playerId(player.getPlayerId())
                    .build();
        }
    }

    public void assignRolesToPlayers(String token, String sessionId) throws Exception {
        String username = jwtTokenService.extractUsername(token);
        JwtToken jwtToken = jwtTokenService.findByTokenAndSession(token, sessionId)
                .orElseThrow(() -> new Exception("Token not found"));

        List<Player> players = playerRepository.findAll();
        Collections.shuffle(players);
        int numberOfImpostors = Math.max(1, players.size() / 4);

        for (int i = 0; i < players.size(); i++) {
            players.get(i).setRole(i < numberOfImpostors ? Role.IMPOSTER : Role.CREWMATE);
            playerRepository.save(players.get(i));
        }
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public void updatePlayerStatus(Long playerId, String status) {
        playerRepositoryService.updatePlayerStatus(playerId, status);
    }
}
