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

        Long playerId = move.getId();
        if (playerId == null) {
            throw new Exception("Player ID is required");
        }

        Player player = _playerService.movePlayer(playerId, move.getDirection());

        return new PlayerPosition(player.getId(), player.getX(), player.getY());
    }
}
