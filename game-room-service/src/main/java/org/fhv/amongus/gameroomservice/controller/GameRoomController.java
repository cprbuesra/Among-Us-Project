package org.fhv.amongus.gameroomservice.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.CreateRoomRequest;
import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.fhv.amongus.gameroomservice.DTO.RoomRequest;
import org.fhv.amongus.gameroomservice.DTO.VoteResultRequest;
import org.fhv.amongus.gameroomservice.DTO.*;
import org.fhv.amongus.gameroomservice.model.Player;
import org.fhv.amongus.gameroomservice.service.GameRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/gameRooms")
public class GameRoomController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRoomService gameRoomService;


    @PostMapping("/createGameRoom")
    public GameRoomDTO createGameRoom(@RequestBody CreateRoomRequest createRoomRequest) throws Exception {

        logger.info("{} created the game room with name: {}", createRoomRequest.getSessionId(), createRoomRequest.getRoomName());
        return gameRoomService.createGameRoom(createRoomRequest.getRoomName(), createRoomRequest.getSessionId());
    }

    @GetMapping("/getGameRooms")
    public List<GameRoomDTO> getGameRooms() {
        logger.info("Getting all game rooms");
        return gameRoomService.getGameRooms();
    }

    @PostMapping("/joinGameRoom")
    public GameRoomDTO joinGameRoom(@RequestBody RoomRequest roomRequest) {
        if (roomRequest.getRoomId() == null || roomRequest.getPlayerId() == null || roomRequest.getUsername() == null) {
            throw new IllegalArgumentException("Room id, player id, and username cannot be empty");
        }

        logger.info("Player with ID: {} and username: {} is joining game room with ID: {}",
                roomRequest.getPlayerId(), roomRequest.getUsername(), roomRequest.getRoomId());

        return gameRoomService.joinGameRoom(roomRequest.getRoomId(), roomRequest.getPlayerId(), roomRequest.getUsername());
    }


    @GetMapping("/getGameRoom/{roomId}")
    public GameRoomDTO getGameRoom(@PathVariable String roomId) {

        Long roomIdLong = Long.parseLong(roomId);
        logger.info("Getting game room with id: {}", roomId);
        return gameRoomService.getGameRoom(roomIdLong);
    }

    @DeleteMapping("/deleteGameRoom")
    public void deleteGameRoom(@RequestParam String roomId, @RequestParam String sessionId, @RequestParam String username) {


        Long roomIdLong = Long.parseLong(roomId);
        logger.info("{} is deleting the game room with id: {}", username, roomIdLong);
        gameRoomService.deleteGameRoom(roomIdLong, sessionId, username);
    }

    @PostMapping("/leaveGameRoom")
    public GameRoomDTO leaveGameRoom(@RequestBody RoomRequest roomRequest) throws Exception {

        if (roomRequest.getRoomId() == null || roomRequest.getPlayerId() == null || roomRequest.getUsername() == null) {
            throw new Exception("Room id, player id and username cannot be empty");
        }
        logger.info("{} is leaving game room {}.", roomRequest.getUsername(), roomRequest.getRoomId());

        return gameRoomService.leaveGameRoom(roomRequest.getRoomId(), roomRequest.getPlayerId(), roomRequest.getUsername());
    }

    @PostMapping("/assignRoles/{roomId}")
    public ResponseEntity<AssignRolesDTO> assignAndFetchRoles(@PathVariable String roomId) throws Exception {
        logger.info("Assigning roles to players in room: {}", roomId);
        Long roomIdLong = Long.parseLong(roomId);
        gameRoomService.assignRolesToPlayers(roomIdLong);
        List<Player> updatedPlayers = gameRoomService.getPlayersByRoomId(roomIdLong);
        logger.info("These are the updated players: {}", updatedPlayers);
        AssignRolesDTO assignRolesDTO = new AssignRolesDTO(updatedPlayers);
        logger.info("Roles assigned to players in room: {}", roomId);
        return ResponseEntity.ok(assignRolesDTO);
    }

    @PostMapping("/killPlayer")
    public ResponseEntity<String> killPlayer (@RequestBody VoteResultRequest request) {
        Long gameRoomId = Long.parseLong(request.getGameRoomId());
        Long votedPlayerIdLong = Long.parseLong(request.getVotedPlayerId());

        gameRoomService.eliminatePlayer(gameRoomId, votedPlayerIdLong);
        return ResponseEntity.ok("Vote result handled");
    }

    /*@PostMapping("/eliminatePlayer")
    public ResponseEntity<String> eliminatePlayer(@RequestBody VoteResultRequest request) {
       logger.info("This is the request: {}", request);
        String votedPlayerId = request.getVotedPlayerId();
        if (votedPlayerId == null || votedPlayerId.equals("skip")) {
            return ResponseEntity.ok("Vote skipped");
        } else {
            logger.warn("Voted player ID is null or doesn't match the expected value");
        }

        Long gameRoomId = Long.parseLong(request.getGameRoomId());
        Long votedPlayerIdLong = Long.parseLong(request.getVotedPlayerId());

        gameRoomService.eliminatePlayer(gameRoomId, votedPlayerIdLong);
        return ResponseEntity.ok("Vote result handled");
    }*/



    @PostMapping("/setPlayerToGhost")
    public ResponseEntity<String> setToGhost(@RequestBody VoteResultRequest request) {
        logger.info("This is the request: {}", request);
        String votedPlayerId = request.getVotedPlayerId();
        if (votedPlayerId == null) {
            return ResponseEntity.ok("No Player needs to be set Ghost");
        } else {
            Long gameRoomId = Long.parseLong(request.getGameRoomId());
            Long votedPlayerIdLong = Long.parseLong(request.getVotedPlayerId());

            gameRoomService.setPlayerToGhost(gameRoomId, votedPlayerIdLong);
            return ResponseEntity.ok("Player set to Ghost!");
        }


    }

    @GetMapping("/getAllOtherPlayersByRoom")
    public List<Player> getAllOtherPlayersByRoom(@RequestParam Long playerId, @RequestParam String roomId) {
        logger.info("Getting all other players in room: {} for player: {}", roomId, playerId);
        Long roomIdLong = Long.parseLong(roomId);
        List<Player> players = gameRoomService.getAllOtherPlayersByRoom(playerId, roomIdLong);
        logger.info("Here are the players: {}", players);
        return players;
    }

    @GetMapping("/getCurrentPlayers/{roomId}")
    public List<PlayerJoinDTO> getCurrentPlayers(@PathVariable String roomId) {
        Long roomIdLong = Long.parseLong(roomId);
        logger.info("Getting current players in room: {}", roomId);
        List<PlayerJoinDTO> players = gameRoomService.getCurrentPlayers(roomIdLong);
        logger.info("Here are the current players: {}", players);
        return players;
    }

    @GetMapping("/getDeadPlayersByRoomId/{roomId}")
    public List<Player> getDeadPlayersByRoomId(@PathVariable String roomId) {
        Long roomIdLong = Long.parseLong(roomId);
        logger.info("Getting dead players in room: {}", roomId);
        List<Player> players = gameRoomService.getAllDeadPlayersByRoomId(roomIdLong);
        logger.info("Here are the dead players: {}", players);
        return players;
    }

    @PutMapping("/updatePlayerStatus")
    public void updatePlayerStatus(@RequestParam Long playerId, @RequestParam Long roomId, @RequestParam String status) {
        logger.info("Updating player status to: {} for player: {} in room: {}", status, playerId, roomId);

        gameRoomService.updatePlayerStatus(playerId, roomId, status);
    }

    @GetMapping("/getAlivePlayersByRoomId/{roomId}")
    public List<Player> getAlivePlayersByRoomId(@PathVariable String roomId) {
        Long roomIdLong = Long.parseLong(roomId);
        logger.info("Getting alive players in room: {}", roomId);
        List<Player> players = gameRoomService.getAllAlivePlayersByRoomId(roomIdLong);

        Set<Player> uniquePlayers = new HashSet<>(players);
        List<Player> uniquePlayersList = new ArrayList<>(uniquePlayers);

        logger.info("Here are the unique alive players: {}", players);
        return uniquePlayersList;
    }

    @PostMapping("/checkWinCondition/{roomId}")
    public void checkWinCondition(@PathVariable String roomId) {
        Long roomIdLong = Long.parseLong(roomId);
        logger.info("Checking win condition for room: {}", roomId);
        gameRoomService.checkWinCondition(roomIdLong);
    }


}
