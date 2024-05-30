package org.fhv.amongus.movementservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.movementservice.DTO.PlayerPosition;
import org.fhv.amongus.movementservice.DTO.PlayerMove;
import org.fhv.amongus.player.player.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovementService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final BoundaryService boundaryService;
    private final PlayerServiceClient playerServiceClient;
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

        if (boundaryService.isWithinMovementBoundaries(absoluteX, absoluteY)) {
            Player player = playerServiceClient.findPlayerById(move.getPlayerId());
            List<Player> otherPlayers = playerServiceClient.findAllOtherPlayers(move.getPlayerId());

            for (Player otherPlayer : otherPlayers){
                logger.info("The player which was send {}" , otherPlayer );

                 if (playerServiceClient.wouldCollideWith(player.getPlayerId(), otherPlayer.getPlayerId())) {
                    logger.info("Player would collide with another player");
                    String targetPlayerId = otherPlayer.getPlayerId().toString();
                    playerPosition.setWouldCollide(true);
                    playerPosition.setTargetPlayerId(targetPlayerId);
                } else {
                    playerPosition.setWouldCollide(false);
                }
            }

            playerPosition.setPlayerId(move.getPlayerId());
            playerPosition.setNewPositionX(newX);
            playerPosition.setNewPositionY(newY);
            playerPosition.setFlip(move.isFlip());
            playerPosition.setSessionId(move.getSessionId());

            logger.info("Player {} moved to new position: ({}, {})", move.getPlayerId(), player.getX(), player.getY());
        } else {
            logger.info("Player {} tried to move out of boundaries", move.getPlayerId());
        }
        return playerPosition;
    }
}
