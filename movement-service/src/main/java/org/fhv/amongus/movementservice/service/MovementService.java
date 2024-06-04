package org.fhv.amongus.movementservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.movementservice.DTO.PlayerPosition;
import org.fhv.amongus.movementservice.DTO.PlayerMove;
import org.fhv.amongus.player.player.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final BoundaryService boundaryService;
    private final PlayerServiceClient playerServiceClient;
    private final GameRoomServiceClient gameRoomServiceClient;
    final int SHIP_WIDTH = 2160;
    final int SHIP_HEIGHT = 1160;

    public PlayerPosition movePlayer(PlayerMove move) throws Exception {
        if (move.getPlayerId() == null || move.getRoomId() == null || move.getDirection() == null) {
            throw new Exception("playerId or direction is null");
        }


        int movementSpeed = 4;
        int newX = move.getPositionX();
        int newY = move.getPositionY();

        switch (move.getDirection().toUpperCase()) {
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
                throw new Exception("Invalid direction");
        }

        int absoluteX = newX + SHIP_WIDTH / 2;
        int absoluteY = newY + SHIP_HEIGHT / 2 - 20;
        PlayerPosition playerPosition = new PlayerPosition();

        if (boundaryService.isWithinMovementBoundaries(absoluteX, absoluteY, move.getStatus())) {
            Player player = playerServiceClient.findPlayerById(move.getPlayerId());
            List<Player> otherPlayers = gameRoomServiceClient.findAllOtherPlayers(move.getPlayerId(), move.getRoomId());

            for (Player otherPlayer : otherPlayers){
                logger.info("The player which is being checked for a collision {}" , otherPlayer );

                 if (playerServiceClient.wouldCollideWith(player.getPlayerId(), otherPlayer.getPlayerId())) {
                    logger.info("Player {} would collide with player {} who is {}", player.getPlayerId(), otherPlayer.getPlayerId(), otherPlayer.getStatus());
                    String targetPlayerId = otherPlayer.getPlayerId().toString();
                    playerPosition.setWouldCollide(true);
                    playerPosition.setTargetPlayerId(targetPlayerId);
                    playerPosition.setStatus(otherPlayer.getStatus());
                } else {
                     logger.info("Player {} with coordinates {} {} would not collide with player {} with coordinates {} {}", player.getPlayerId(), player.getX(), player.getY(), otherPlayer.getPlayerId(), otherPlayer.getX(), otherPlayer.getX());
                }
            }

            playerPosition.setPlayerId(move.getPlayerId());
            playerPosition.setNewPositionX(newX);
            playerPosition.setNewPositionY(newY);
            playerPosition.setFlip(move.isFlip());
            playerPosition.setSessionId(move.getSessionId());
            playerPosition.setUsername(move.getUsername());

            logger.info("Player {} moved to new position: ({}, {})", move.getPlayerId(), player.getX(), player.getY());
        } else {
            logger.info("Player {} tried to move out of boundaries with this move {} {}", move.getPlayerId(), move.getPositionX(), move.getPositionY());
        }
        return playerPosition;
    }
}
