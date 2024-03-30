package org.fhv.amongus.amongus.user;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService _playerService;



    @PostMapping("/save")
    public Player savePlayer(@RequestBody Player player) {
        return _playerService.savePlayer(player);
    }

    @PostMapping("/move")
    public Player movePlayer(@RequestBody MoveRequest moveRequest) {
        return _playerService.movePlayer(moveRequest.getPlayerId(), moveRequest.getDirection());
    }
}
