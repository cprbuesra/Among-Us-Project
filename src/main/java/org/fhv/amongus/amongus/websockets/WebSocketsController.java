package org.fhv.amongus.amongus.websockets;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.gameRoom.DTO.GameRoomDTO;
import org.fhv.amongus.amongus.gameRoom.model.GameRoom;
import org.fhv.amongus.amongus.gameRoom.service.GameRoomService;
import org.fhv.amongus.amongus.jwt.JwtService;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.player.DTO.PlayerInfo;
import org.fhv.amongus.amongus.player.DTO.PlayerInfos;
import org.fhv.amongus.amongus.player.DTO.PlayerMove;
import org.fhv.amongus.amongus.player.DTO.PlayerPosition;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class WebSocketsController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final PlayerService playerService;
    private final JwtService jwtService;
    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/move")
    public PlayerPosition movePlayer(@Payload PlayerMove move) throws Exception {
        String username = jwtService.extractUsername(move.getToken());

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(move.getToken(), move.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));

        String roomId = move.getRoomId();

        Player player = playerService.movePlayer(username, move.getDirection(), move.isFlip());
        PlayerPosition playerPosition = new PlayerPosition(jwtTokenObj.getSessionId(), player.getX(), player.getY(), player.isFlip());

        logger.info("Player {} moved: {}, in room {}", username, playerPosition, roomId);

        simpMessagingTemplate.convertAndSend("/topic/move/" + roomId, playerPosition);
        return playerPosition;
    }

    @MessageMapping("/moveEnd")
    public void moveEnd(@Payload PlayerMove move) throws Exception {
        String username = jwtService.extractUsername(move.getToken());

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(move.getToken(), move.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));

        String roomId = move.getRoomId();
        String sessionId = jwtTokenObj.getSessionId();

        logger.info("Player {} has stopped moving in room {}", username, roomId);

        simpMessagingTemplate.convertAndSend("/topic/moveEnd/" + roomId, "{\"sessionId\": \"" + sessionId + "\"}");
    }


    @MessageMapping("/leave")
    @SendTo("/topic/leave")
    public String leaveGame(@Payload PlayerInfo playerInfo) throws Exception {
        String username = jwtService.extractUsername(playerInfo.getToken());

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(playerInfo.getToken(), playerInfo.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));

        playerService.leaveGame(username);

        String sessionId = playerInfo.getSessionId();
        return "{\"sessionId\": \"" + sessionId + "\"}";
    }

    @MessageMapping("/startGame")
    public GameRoomDTO startGame(@Payload PlayerInfos playerInfo) throws Exception {

        System.out.println(playerInfo);
        String username = jwtService.extractUsername(playerInfo.getToken());

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(playerInfo.getToken(), playerInfo.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));

        Long roomIdLong = Long.parseLong(playerInfo.getRoomId());
        GameRoomDTO gameRoomDTO = gameRoomService.startGame(roomIdLong);

        logger.info("Player {} has started the game in room {}", username, roomIdLong);

        simpMessagingTemplate.convertAndSend("/topic/startGame" + playerInfo.getRoomId(), gameRoomDTO);
        return gameRoomDTO;
    }
}
