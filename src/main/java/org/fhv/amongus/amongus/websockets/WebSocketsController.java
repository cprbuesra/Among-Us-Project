package org.fhv.amongus.amongus.websockets;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.jwt.JwtService;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.player.DTO.PlayerMove;
import org.fhv.amongus.amongus.player.DTO.PlayerPosition;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.service.PlayerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketsController {

    private final PlayerService _playerService;
    private final JwtService _jwtService;


    @MessageMapping("/move")
    @SendTo("/topic/move")
    public PlayerPosition movePlayer(@Payload PlayerMove move) throws Exception {
        System.out.println("Token: " + move.getToken());

        String username = _jwtService.extractUsername(move.getToken());
        System.out.println("Username: " + username);

        JwtToken jwtTokenObj = _jwtService.findByTokenAndSession(move.getToken(), move.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));

        System.out.println("Retrieved token: " + jwtTokenObj.getToken());
        Player player = _playerService.movePlayer(username, move.getDirection(), move.isFlip());

        return new PlayerPosition(move.getToken(), move.getSessionId(), player.getX(), player.getY(), player.isFlip());
    }
}
