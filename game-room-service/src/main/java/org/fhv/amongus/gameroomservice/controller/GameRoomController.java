package org.fhv.amongus.gameroomservice.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.*;
import org.fhv.amongus.gameroomservice.model.Player;
import org.fhv.amongus.gameroomservice.service.GameRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/gameRooms")
public class GameRoomController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRoomService gameRoomService;
    private SimpMessagingTemplate simpMessagingTemplate;

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
    public void deleteGameRoom(@RequestParam String roomId, @RequestParam String sessionId, @RequestParam String username) throws Exception {


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

    @PostMapping("/assignRoles")
    public ResponseEntity<AssignRolesDTO> assignAndFetchRoles(@RequestBody AssignRoles assignRoles) throws Exception {
        gameRoomService.assignRolesToPlayers(assignRoles.getToken(), assignRoles.getSessionId(), assignRoles.getRoomId());
        List<Player> updatedPlayers = gameRoomService.getPlayersByRoomId(assignRoles.getRoomId());
        AssignRolesDTO assignRolesDTO = new AssignRolesDTO(assignRoles.getSessionId(), updatedPlayers);
        return ResponseEntity.ok(assignRolesDTO);
    }


}
