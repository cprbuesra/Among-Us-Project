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

    public List<PlayerInfo> updateRoles(List<Player> players) {
        logger.info("These are the players: {}", players);

        Collections.shuffle(players);

        int totalPlayers = players.size();
        int numberOfImpostors = Math.max(1, totalPlayers / 3);
        List<PlayerInfo> playerInfos = new ArrayList<>();

        for (int i = 0; i < totalPlayers; i++) {
            Player currentPlayer = players.get(i);

            // Fetch the player from the database to get the current positions
            Player playerFromDb = playerRepository.findById(currentPlayer.getPlayerId()).orElse(null);
            if (playerFromDb == null) {
                continue;
            }

            // Get current x and y positions from the database
            int currentX = playerFromDb.getX();
            int currentY = playerFromDb.getY();

            if (i < numberOfImpostors) {
                currentPlayer.setRole(Role.IMPOSTER);
                logger.info("Player {} is an impostor", currentPlayer.getUsername());
            } else {
                currentPlayer.setRole(Role.CREWMATE);
                logger.info("Player {} is a crewmate", currentPlayer.getUsername());
            }

            // Set positions to the current positions fetched from the database
            currentPlayer.setX(currentX);
            currentPlayer.setY(currentY);

            logger.info("This is the updated player: {}", currentPlayer);
            playerRepository.save(currentPlayer);

            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setPlayerId(currentPlayer.getPlayerId());
            playerInfo.setUsername(currentPlayer.getUsername());
            playerInfo.setRole(String.valueOf(currentPlayer.getRole()));

            playerInfos.add(playerInfo);
        }

        return playerInfos;
    }


    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }


    public boolean wouldCollideWith(Player player, Player otherPlayer) {
        final int COLLISION_THRESHOLD = 30;

        logger.info("This is the position of the player {} {}", player.getX(), player.getY());
        logger.info("This is the position of the other player {} {}", otherPlayer.getX(), otherPlayer.getY());

        int deltaX = player.getX() - otherPlayer.getX();
        int deltaY = player.getY() - otherPlayer.getY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        logger.info("Distance between players: {}", distance);

        return distance <= COLLISION_THRESHOLD;
    }


    public List<Player> getAllOtherPlayers(Long playerId) {
        return playerRepositoryService.findAllOtherPlayers(playerId);
    }

    public void updatePlayerStatus(Long playerId, String status) {
        playerRepositoryService.updatePlayerStatus(playerId, status);
    }
}
