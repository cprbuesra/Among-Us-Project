package org.fhv.amongus.movementservice;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.movementservice.DTO.PlayerEndMove;
import org.fhv.amongus.movementservice.DTO.PlayerMove;
import org.fhv.amongus.movementservice.DTO.PlayerPosition;
import org.fhv.amongus.movementservice.DTO.PlayerPositionUpdateRequest;
import org.fhv.amongus.movementservice.service.MovementMessageSender;
import org.fhv.amongus.movementservice.service.MovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MovementController {

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

    @PostMapping("/checkNearPlayer")
    public ResponseEntity<Boolean> checkNearPlayer(@RequestBody PlayerMove move) {
        try {
            boolean isNear = movementService.movePlayer(move).isFlip();
            String topic = "/topic/nearPlayer/" + move.getRoomId();
            simpMessagingTemplate.convertAndSend(topic, isNear);
            return ResponseEntity.ok(isNear);
        } catch (Exception e) {
            logger.error("Error checking near player", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
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
