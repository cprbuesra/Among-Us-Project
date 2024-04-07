package org.fhv.amongus.amongus.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> savePlayer(@RequestBody Player player) {
        try {
            Player savedPlayer = _playerService.savePlayer(player);
            return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/move")
    public Player movePlayer(@RequestBody MoveRequest moveRequest) {
        return _playerService.movePlayer(moveRequest.getId(), moveRequest.getDirection());
    }
}
