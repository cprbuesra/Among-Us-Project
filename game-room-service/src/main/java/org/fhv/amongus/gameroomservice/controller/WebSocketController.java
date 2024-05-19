package org.fhv.amongus.gameroomservice.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.fhv.amongus.gameroomservice.DTO.RoomRequest;
import org.fhv.amongus.gameroomservice.service.GameRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/startGame")
    public GameRoomDTO startGame(@RequestBody RoomRequest roomRequest) throws Exception {
        logger.info("Received start game request for room {}", roomRequest);

        if (roomRequest.getRoomId() == null || roomRequest.getPlayerId() == null || roomRequest.getUsername() == null) {
            throw new Exception("Room id, player id and username cannot be empty");
        }

        GameRoomDTO gameRoomDTO = gameRoomService.startGame(roomRequest.getRoomId());
        logger.info("{} has started the game in room {}", roomRequest.getUsername(), roomRequest.getRoomId());
        simpMessagingTemplate.convertAndSend("/topic/startGame/" + roomRequest.getRoomId(), gameRoomDTO);
        return gameRoomDTO;
    }
}
