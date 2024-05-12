package org.fhv.amongus.amongus.player.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.boundaries.BoundaryService;
import org.fhv.amongus.amongus.exceptions.PlayerNotFoundException;
import org.fhv.amongus.amongus.gameRoom.model.GameRoom;
import org.fhv.amongus.amongus.gameRoom.service.GameRoomRepositoryService;
import org.fhv.amongus.amongus.jwt.JwtService;
import org.fhv.amongus.amongus.jwt.JwtTokeRepositoryService;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.player.DTO.AuthenticationResponse;
import org.fhv.amongus.amongus.player.DTO.RegisterRequest;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.model.Role;
import org.fhv.amongus.amongus.player.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final JwtTokeRepositoryService jwtTokeRepositoryService;
    private final PlayerRepository playerRepository;
    private final JwtService jwtService;
    private final GameRoomRepositoryService gameRoomRepositoryService;
    private final BoundaryService boundaryService;
    final int SHIP_WIDTH = 2160;
    final int SHIP_HEIGHT = 1160;
    private final PlayerRepositoryService playerRepositoryService;

    public AuthenticationResponse savePlayer(RegisterRequest registerRequest) {
        Optional<Player> existingPlayer = playerRepository.findByUsername(registerRequest.getUsername());
        if (existingPlayer.isPresent()) {
            logger.warn("Player with Username: {} already exists with ID: {}", registerRequest.getUsername(), existingPlayer.get().getPlayerId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player already exists");
        } else {
            logger.info("Saving player with Username: {}", registerRequest.getUsername());
            var player = Player.builder()
                    .username(registerRequest.getUsername())
                    .x(335)
                    .y(20)
                    .flip(false)
                    .build();
            playerRepository.save(player);


            var jwtToken = jwtService.generateToken(player);
            logger.info("Generated JWT Token: {}", jwtToken);
            var expiryDate = jwtService.extractExpiration(jwtToken);
            String sessionId = UUID.randomUUID().toString();

            JwtToken jwtTokenObj = JwtToken.builder()
                    .token(jwtToken)
                    .player(player)
                    .expirationDate(expiryDate)
                    .sessionId(sessionId)
                    .build();

            jwtTokeRepositoryService.save(jwtTokenObj);
            logger.info("Saved JWT Token: {}", jwtTokenObj);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .sessionId(sessionId)
                    .playerId(player.getPlayerId())
                    .build();
        }
    }

    public Player movePlayer(String username, String direction, boolean flip) {
        if (username == null || direction == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or direction is null");
        }

        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        int movementSpeed = 4;
        int newX = player.getX();
        int newY = player.getY();

        switch (direction.toUpperCase()) {
            case "UP":
                newY -= movementSpeed;
                break;
            case "DOWN":
                newY += movementSpeed;
                break;
            case "LEFT":
                newX -= movementSpeed;
                break;
            case "RIGHT":
                newX += movementSpeed;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid direction");
        }

        int absoluteX = newX + SHIP_WIDTH / 2;
        int absoluteY = newY + SHIP_HEIGHT / 2 - 20;

        if (boundaryService.isWithinMovementBoundaries(absoluteX, absoluteY)) {
            player.setX(newX);
            player.setY(newY);
            player.setFlip(flip);
        }
        return playerRepository.save(player);
    }

    @Transactional
    public void leaveGame(String username) throws Exception {
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is null");
        }

        Player player = playerRepositoryService.findByUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        GameRoom gameRoom = player.getGameRoom();

        JwtToken jwtToken = jwtTokeRepositoryService.findByPlayer(player)
                .orElseThrow(() -> new Exception("Token not found"));

        logger.info("Deleting player: {}", player);
        jwtTokeRepositoryService.deleteByPlayer(player);
        gameRoomRepositoryService.leaveGameRoom(gameRoom.getId(), jwtToken.getToken(), jwtToken.getSessionId());
        playerRepositoryService.deletePlayer(player);
    }



    public void assignRolesToPlayers(String token, String sessionId) throws Exception {
        String username = jwtService.extractUsername(token);
        JwtToken jwtToken = jwtService.findByTokenAndSession(token, sessionId)
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




}