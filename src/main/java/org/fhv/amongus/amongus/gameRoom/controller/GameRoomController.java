package org.fhv.amongus.amongus.gameRoom.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.gameRoom.DTO.CreateRoomRequest;
import org.fhv.amongus.amongus.gameRoom.DTO.GameRoomDTO;
import org.fhv.amongus.amongus.gameRoom.DTO.RoomRequest;
import org.fhv.amongus.amongus.gameRoom.service.GameRoomService;
import org.fhv.amongus.amongus.jwt.JwtService;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/gameRooms")
@RequiredArgsConstructor
public class GameRoomController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRoomService gameRoomService;
    private final JwtService jwtService;

    @PostMapping("/createGameRoom")
    public GameRoomDTO createGameRoom(@RequestBody CreateRoomRequest createRoomRequest) throws Exception {
        String username = jwtService.extractUsername(createRoomRequest.getToken());

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(createRoomRequest.getToken(), createRoomRequest.getSessionId())
                .orElseThrow(() -> new Exception("JWT Token not found"));


        if (createRoomRequest.getRoomName() == null) {
            throw new IllegalArgumentException("Room name cannot be empty");
        }

        logger.info("{} created the game room with name: {}", username, createRoomRequest.getRoomName());
        return gameRoomService.createGameRoom(createRoomRequest.getRoomName(), createRoomRequest.getSessionId());
    }

    @GetMapping("/getGameRooms")
    public List<GameRoomDTO> getGameRooms() {

        logger.info("Getting all game rooms");
        return gameRoomService.getGameRooms();
    }

    @PostMapping("/joinGameRoom")
    public GameRoomDTO joinGameRoom(@RequestBody RoomRequest roomRequest) {
        if (roomRequest.getRoomId() == null || roomRequest.getToken() == null || roomRequest.getSessionId() == null) {
            throw new IllegalArgumentException("Room id, token and session id cannot be empty");
        }

        logger.info("Joining game room with id: {}, token: {}, session id: {}", roomRequest.getRoomId(), roomRequest.getToken(), roomRequest.getSessionId());

        return gameRoomService.joinGameRoom(roomRequest.getRoomId(), roomRequest.getToken(), roomRequest.getSessionId());
    }

    @GetMapping("/getGameRoom/{roomId}")
    public GameRoomDTO getGameRoom(@PathVariable String roomId) {

        Long roomIdLong = Long.parseLong(roomId);
        logger.info("Getting game room with id: {}", roomId);
        return gameRoomService.getGameRoom(roomIdLong);
    }

    @DeleteMapping("/deleteGameRoom")
    public void deleteGameRoom(@RequestParam String roomId, @RequestParam String token, @RequestParam String sessionId) throws Exception {
        String username = jwtService.extractUsername(token);

        JwtToken jwtTokenObj = jwtService.findByTokenAndSession(token, sessionId)
                .orElseThrow(() -> new Exception("JWT Token not found"));

        Long roomIdLong = Long.parseLong(roomId);
        logger.info("{} is deleting the game room with id: {}", username, roomIdLong);
        gameRoomService.deleteGameRoom(roomIdLong, sessionId);
    }

    @PostMapping("/leaveGameRoom")
    public GameRoomDTO leaveGameRoom(@RequestBody RoomRequest roomRequest) throws Exception {
        if (roomRequest.getRoomId() == null || roomRequest.getToken() == null || roomRequest.getSessionId() == null) {
            throw new IllegalArgumentException("Room id, token and session id cannot be empty");
        }

        String username = jwtService.extractUsername(roomRequest.getToken());
        logger.info("{} is leaving game room with id: {}.", username, roomRequest.getRoomId());

        return gameRoomService.leaveGameRoom(roomRequest.getRoomId(), roomRequest.getToken(), roomRequest.getSessionId());
    }



}
