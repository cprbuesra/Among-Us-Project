package org.fhv.amongus.movementservice.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.movementservice.DTO.PlayerEndMove;
import org.fhv.amongus.movementservice.DTO.PlayerMove;
import org.fhv.amongus.movementservice.DTO.PlayerPosition;
import org.fhv.amongus.movementservice.DTO.PlayerPositionUpdateRequest;
import org.fhv.amongus.movementservice.service.MovementMessageSender;
import org.fhv.amongus.movementservice.service.MovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MovementService movementService;
    private final MovementMessageSender movementMessageSender;
    private final SimpMessagingTemplate simpMessagingTemplate;
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @MessageMapping("/move")
    public void movePlayer(@Payload PlayerMove move) throws Exception {

        PlayerPosition playerPosition = movementService.movePlayer(move);
        simpMessagingTemplate.convertAndSend("/topic/move/" + move.getRoomId(), playerPosition);
        logger.info("Player {} moved: {} {}, in room {}", move.getPlayerId(), playerPosition.getNewPositionY(), playerPosition.getNewPositionY(), move.getRoomId());

        PlayerPositionUpdateRequest updateRequest = new PlayerPositionUpdateRequest(
                playerPosition.getPlayerId(),
                playerPosition.getNewPositionX(),
                playerPosition.getNewPositionY(),
                playerPosition.isFlip()
        );

        movementMessageSender.sendPlayerPositionUpdate(updateRequest);
    }

    @MessageMapping("/moveEnd")
    public void moveEnd(@Payload PlayerEndMove endMove) throws Exception {

        String username = endMove.getUsername();
        String roomId = endMove.getRoomId();
        String sessionId = endMove.getSessionId();

        logger.info("Player {} has stopped moving in room {}", username, roomId);
        logger.info("Session ID: {}", sessionId);

        simpMessagingTemplate.convertAndSend("/topic/moveEnd/" + roomId, "{\"sessionId\": \"" + sessionId + "\"}");
    }

    /*
    @MessageMapping("/leave")
    @SendTo("/topic/leave")
    public String leaveGame(@Payload PlayerInfo playerInfo) throws Exception {


        playerService.leaveGame(username);

        String sessionId = playerInfo.getSessionId();
        return "{\"sessionId\": \"" + sessionId + "\"}";
    }*/
}
