package org.fhv.amongus.player.player.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.jwt.model.JwtToken;
import org.fhv.amongus.player.jwt.service.JwtTokenRepositoryService;
import org.fhv.amongus.player.jwt.service.JwtTokenService;
import org.fhv.amongus.player.player.model.Action;
import org.fhv.amongus.player.player.DTO.PlayerInfo;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.model.Role;
import org.fhv.amongus.player.player.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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


    public List<PlayerInfo> updateRoles(List<Player> players){
        Collections.shuffle(players);

        int totalPlayers = players.size();
        int numberOfImpostors = Math.max(1, totalPlayers / 3);
        List<PlayerInfo> playerInfos = new ArrayList<>();
        for (int i = 0; i < totalPlayers; i++) {
            if (i < numberOfImpostors) {
                players.get(i).setRole(Role.IMPOSTER);
            } else {
                players.get(i).setRole(Role.CREWMATE);
            }
            playerRepository.save(players.get(i));

            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setPlayerId(players.get(i).getPlayerId());
            playerInfo.setUsername(players.get(i).getUsername());
            playerInfo.setRole(String.valueOf(players.get(i).getRole()));


            playerInfos.add(playerInfo);
        }
        return playerInfos;
    }


    public void performAction(Player player, Action action, Player targetPlayer) {
        validatePlayer(player);
        switch (action) {
            case KILL:
                eliminatePlayer(player, targetPlayer, action);
                break;
            // other action cases
        }
    }

    public void validatePlayer(Player player) {
        if (!playerRepository.existsById(player.getPlayerId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found");
        }
    }

    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }


    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public double calculateDistance(Player player, Player otherPlayer) {
        int xDistance = Math.abs(player.getX() - otherPlayer.getX());
        int yDistance = Math.abs(player.getY() - otherPlayer.getY());
        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
    }

    public void eliminatePlayer(Player player, Player otherPlayer, Action action) {
        player.eliminatePlayer(otherPlayer, action);
    }

    public boolean wouldCollideWith(Player player, Player otherPlayer, int newX, int newY) {
        return otherPlayer.getX() == newX && otherPlayer.getY() == newY;
    }
}
