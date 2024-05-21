package org.fhv.amongus.player.player.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.player.DTO.*;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/player")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/save")
    public ResponseEntity<AuthResponse> savePlayer(@RequestBody AuthRequest registerRequest) {
        try {
            AuthResponse response = playerService.savePlayer(registerRequest);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/assignRoles")
    public ResponseEntity<AssignRolesDTO> assignAndFetchRoles(@RequestBody AssignRoles assignRoles) {
        try {
            playerService.assignRolesToPlayers(assignRoles.getToken(), assignRoles.getSessionId());
            List<Player> updatedPlayers = playerService.getAllPlayers();
            AssignRolesDTO response = new AssignRolesDTO(assignRoles.getSessionId(), updatedPlayers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/action")
    public ResponseEntity<Void> performAction(@RequestBody ActionRequest actionRequest) {
        try {
            playerService.performAction(actionRequest.getPlayer(), actionRequest.getAction(), actionRequest.getTargetPlayer());
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/isNear")
    public ResponseEntity<Boolean> calculateDistance(@RequestBody PlayerDistanceRequest request) {
        try {
            double distance = playerService.calculateDistance(request.getPlayer1(), request.getPlayer2());
            boolean isNear = distance <= Player.NEAR_DISTANCE;
            return ResponseEntity.ok(isNear);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/check-collision")
    public ResponseEntity<Boolean> checkCollision(@RequestBody CollisionRequest collisionRequest) {
        try {
            Player player = playerService.getPlayerById(collisionRequest.getPlayerId());
            Player otherPlayer = playerService.getPlayerById(collisionRequest.getOtherPlayerId());
            boolean collision = playerService.wouldCollideWith(player, otherPlayer, collisionRequest.getNewX(), collisionRequest.getNewY());
            return ResponseEntity.ok(collision);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @PostMapping("/eliminate")
    public ResponseEntity<Void> eliminatePlayer(@RequestBody EliminationRequest eliminationRequest) {
        try {
            Player player = playerService.getPlayerById(eliminationRequest.getPlayerId());
            Player targetPlayer = playerService.getPlayerById(eliminationRequest.getTargetPlayerId());
            playerService.eliminatePlayer(player, targetPlayer, eliminationRequest.getAction());
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
