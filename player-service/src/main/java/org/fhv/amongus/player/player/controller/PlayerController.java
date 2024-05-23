package org.fhv.amongus.player.player.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.jwt.model.JwtToken;
import org.fhv.amongus.player.jwt.service.JwtTokenService;
import org.fhv.amongus.player.player.DTO.ActionRequest;
import org.fhv.amongus.player.player.DTO.CollisionRequest;
import org.fhv.amongus.player.player.DTO.PlayerInfo;
import org.fhv.amongus.player.player.model.Action;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/player")
public class PlayerController {

    private final PlayerService playerService;
    private final JwtTokenService jwtTokenService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @PostMapping("/save")
    public ResponseEntity<AuthResponse> savePlayer(@RequestBody AuthRequest registerRequest) {
        return ResponseEntity.ok(playerService.savePlayer(registerRequest));
    }


    @PostMapping("/updatePlayerRoles")
    public List<PlayerInfo> assignAndFetchRoles(@RequestBody List<Player> players) throws Exception {
       return playerService.updateRoles(players);
    }
    @PostMapping("/performAction")
    public ResponseEntity<Void> performAction(@RequestBody ActionRequest actionRequest) {
        Player player = playerService.getPlayerById(actionRequest.getPlayerId().getPlayerId());
        Player targetPlayer = playerService.getPlayerById(actionRequest.getTargetPlayerId().getPlayerId());
        Action action = actionRequest.getAction();

        playerService.performAction(player, action, targetPlayer);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/calculateDistance")
    public ResponseEntity<Double> calculateDistance(@RequestBody ActionRequest actionRequest) {
        Player player = playerService.getPlayerById(actionRequest.getPlayerId().getPlayerId());
        Player targetPlayer = playerService.getPlayerById(actionRequest.getTargetPlayerId().getPlayerId());

        return ResponseEntity.ok(playerService.calculateDistance(player, targetPlayer));
    }
    @PostMapping("/wouldCollideWith")
    public ResponseEntity<Boolean> wouldCollideWith(@RequestBody CollisionRequest collisionRequest) {
        logger.info("Checking if player {} would collide with player {}", collisionRequest.getPlayerId(), collisionRequest.getOtherPlayerId());

        Player player = playerService.getPlayerById(collisionRequest.getPlayerId());
        Player targetPlayer = playerService.getPlayerById(collisionRequest.getOtherPlayerId());

        boolean wouldCollide = playerService.wouldCollideWith(player, targetPlayer);
        System.out.println(wouldCollide);

        return ResponseEntity.ok(wouldCollide);
    }

    @GetMapping("/getAllPlayers/{playerId}")
    public ResponseEntity<List<Player>> getAllPlayers(@PathVariable Long playerId) {
        List<Player> players = playerService.getAllOtherPlayers(playerId);
        logger.info("Here are the players: {}", players);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/getPlayerById/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long playerId) {
        logger.info("Getting player with ID: {}", playerId);
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player);
    }


    @PostMapping("/eliminatePlayer")
    public ResponseEntity<Void> eliminatePlayer(@RequestBody ActionRequest actionRequest) {
        Player player = playerService.getPlayerById(actionRequest.getPlayerId().getPlayerId());
        Player targetPlayer = playerService.getPlayerById(actionRequest.getTargetPlayerId().getPlayerId());
        Action action = actionRequest.getAction();

        playerService.eliminatePlayer(player, targetPlayer, action);

        return ResponseEntity.ok().build();
    }
}
