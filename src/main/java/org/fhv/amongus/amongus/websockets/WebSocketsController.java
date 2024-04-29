package org.fhv.amongus.amongus.websockets;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.jwt.JwtService;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.player.DTO.PlayerInfo;
import org.fhv.amongus.amongus.player.DTO.PlayerMove;
import org.fhv.amongus.amongus.player.DTO.PlayerPosition;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class WebSocketsController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final PlayerService playerService;
    private final JwtService jwtService;


    @MessageMapping("/move")
    @SendTo("/topic/move")
    public PlayerPosition movePlayer(@Payload PlayerMove move) throws Exception {
        String username = jwtService.extractUsername(move.getToken());
        logger.info("Username: {}", username);

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(move.getToken(), move.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));

        logger.info("Retrieved token: {}", jwtTokenObj.getToken());
        Player player = playerService.movePlayer(username, move.getDirection(), move.isFlip());
        PlayerPosition playerPosition = new PlayerPosition(jwtTokenObj.getSessionId(), player.getX(), player.getY(), player.isFlip());
        logger.info("Player moved: {}", playerPosition);
        return playerPosition;
    }

    @MessageMapping("/moveEnd")
    @SendTo("/topic/moveEnd")
    public String moveEnd(@Payload PlayerMove move) throws Exception {
        String username = jwtService.extractUsername(move.getToken());

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(move.getToken(), move.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));
        return "{\"sessionId\": \"" + jwtTokenObj.getSessionId() + "\"}";
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
}
