package org.fhv.amongus.amongus.player.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.player.DTO.*;
import org.fhv.amongus.amongus.player.service.PlayerService;
import org.fhv.amongus.amongus.player.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/player")
public class PlayerController {

    private final PlayerService _playerService;

    @PostMapping("/save")
    public ResponseEntity<AuthenticationResponse> savePlayer(@RequestBody RegisterRequest registerRequest) {
            return ResponseEntity.ok(_playerService.savePlayer(registerRequest));

    }

    @PostMapping("/assignRoles")
    public AssignRolesDTO assignAndFetchRoles(@RequestBody AssignRoles assignRoles) throws Exception {
        _playerService.assignRolesToPlayers(assignRoles.getToken(), assignRoles.getSessionId());
        List<Player> updatedPlayers = _playerService.getAllPlayers();
        return new AssignRolesDTO(assignRoles.getSessionId(), updatedPlayers);
    }
}
