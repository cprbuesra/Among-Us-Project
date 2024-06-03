package org.fhv.amongus.gameroomservice.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.fhv.amongus.gameroomservice.DTO.JoinRoomDTO;
import org.fhv.amongus.gameroomservice.DTO.PlayerJoinDTO;
import org.fhv.amongus.gameroomservice.DTO.RoomRequest;
import org.fhv.amongus.gameroomservice.service.GameRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/startGame")
    public void startGame(@Payload RoomRequest roomRequest) throws Exception {
        logger.info("Received start game request for room {}", roomRequest);

        if (roomRequest.getRoomId() == null || roomRequest.getPlayerId() == null || roomRequest.getUsername() == null) {
            throw new Exception("Room id, player id and username cannot be empty");
        }


        GameRoomDTO gameRoomDTO = gameRoomService.startGame(roomRequest.getRoomId());
        logger.info("{} has started the game in room {}", roomRequest.getUsername(), roomRequest.getRoomId());
        simpMessagingTemplate.convertAndSend("/topic/startGame/" + roomRequest.getRoomId(), gameRoomDTO);
    }

    @MessageMapping("/join")
    public void joinGameRoom(RoomRequest roomRequest) {

        logger.info("{} is joining the game room {}", roomRequest.getUsername(), roomRequest.getRoomId());
        String role = gameRoomService.getRole(roomRequest.getUsername());
        String playerId = roomRequest.getPlayerId().toString();

        List<PlayerJoinDTO> currentPlayers = gameRoomService.getCurrentPlayers(roomRequest.getRoomId());

        String roomId = roomRequest.getRoomId().toString();
        JoinRoomDTO joinRoomDTO = new JoinRoomDTO(playerId, role, roomRequest.getUsername(), currentPlayers);

        simpMessagingTemplate.convertAndSend("/topic/join/" + roomId, joinRoomDTO);
    }
}
