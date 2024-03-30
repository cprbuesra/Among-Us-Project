package org.fhv.amongus.amongus.websockets;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.user.PlayerMove;
import org.fhv.amongus.amongus.user.PlayerPosition;
import org.fhv.amongus.amongus.user.Player;
import org.fhv.amongus.amongus.user.PlayerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketsController {

    private final PlayerService _playerService;


    @MessageMapping("/move")
    @SendTo("/topic/move")
    public PlayerPosition movePlayer(PlayerMove move) throws Exception {
        Player player = _playerService.movePlayer(move.getPlayerId(), move.getDirection());

        return new PlayerPosition(player.getId(), player.getX(), player.getY());
    }
}
