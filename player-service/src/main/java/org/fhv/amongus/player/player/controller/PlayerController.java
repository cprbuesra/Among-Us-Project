package org.fhv.amongus.player.player.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.player.DTO.AssignRoles;
import org.fhv.amongus.player.player.DTO.AssignRolesDTO;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.service.PlayerService;
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

    private final PlayerService playerService;

    @PostMapping("/save")
    public ResponseEntity<AuthResponse> savePlayer(@RequestBody AuthRequest registerRequest) {
        return ResponseEntity.ok(playerService.savePlayer(registerRequest));
    }

    @PostMapping("/assignRoles")
    public ResponseEntity<AssignRolesDTO> assignAndFetchRoles(@RequestBody AssignRoles assignRoles) throws Exception {
        playerService.assignRolesToPlayers(assignRoles.getToken(), assignRoles.getSessionId(), assignRoles.getRoomId());
        List<Player> updatedPlayers = playerService.getPlayersByRoomId(assignRoles.getRoomId());
        AssignRolesDTO assignRolesDTO = new AssignRolesDTO(assignRoles.getSessionId(), updatedPlayers);
        return ResponseEntity.ok(assignRolesDTO);
    }

    @PostMapping("/updatePlayerRoles")
    public ResponseEntity<AssignRolesDTO> assignAndFetchRoles(@RequestBody AssignRoles assignRoles) throws Exception {
        playerService.assignRolesToPlayers(assignRoles.getToken(), assignRoles.getSessionId(), assignRoles.getRoomId());
        List<Player> updatedPlayers = playerService.getPlayersByRoomId(assignRoles.getRoomId());
        AssignRolesDTO assignRolesDTO = new AssignRolesDTO(assignRoles.getSessionId(), updatedPlayers);
        return ResponseEntity.ok(assignRolesDTO);
    }

}
