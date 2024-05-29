package org.fhv.amongus.player.player.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.player.DTO.AssignRoles;
import org.fhv.amongus.player.player.DTO.AssignRolesDTO;
import org.fhv.amongus.player.player.DTO.PlayerStatusUpdateRequest;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/player")
public class PlayerController {

    private final PlayerService playerService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/save")
    public ResponseEntity<AuthResponse> savePlayer(@RequestBody AuthRequest registerRequest) {
        return ResponseEntity.ok(playerService.savePlayer(registerRequest));
    }

    @PostMapping("/assignRoles")
    public AssignRolesDTO assignAndFetchRoles(@RequestBody AssignRoles assignRoles) throws Exception {
        playerService.assignRolesToPlayers(assignRoles.getToken(), assignRoles.getSessionId());
        List<Player> updatedPlayers = playerService.getAllPlayers();
        return new AssignRolesDTO(assignRoles.getSessionId(), updatedPlayers);
    }

    @PutMapping("/status")
    public ResponseEntity<String> updatePlayerStatus(@RequestBody PlayerStatusUpdateRequest request) {

        logger.info("Updating player status with this request: {}", request);
        Long playerId = Long.parseLong(request.getPlayerId());

        playerService.updatePlayerStatus(playerId, request.getStatus());
        return ResponseEntity.ok("Player status updated");
    }
}
