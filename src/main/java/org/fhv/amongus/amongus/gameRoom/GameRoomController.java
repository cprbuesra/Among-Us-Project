package org.fhv.amongus.amongus.gameRoom;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.gameRoom.DTO.RoomRequest;
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

    @PostMapping("/createGameRoom")
    public GameRoom createGameRoom(@RequestBody String roomName) {
        if (roomName == null) {
            throw new IllegalArgumentException("Room name cannot be empty");
        }

        logger.info("Creating game room with name: {}", roomName);
        return gameRoomService.createGameRoom(roomName);
    }

    @GetMapping("/getGameRooms")
    public List<GameRoom> getGameRooms() {

        logger.info("Getting all game rooms");
        return gameRoomService.getGameRooms();
    }

    @PostMapping("/joinGameRoom")
    public GameRoom joinGameRoom(@RequestBody RoomRequest roomRequest) {
        if (roomRequest.getRoomId() == null || roomRequest.getToken() == null || roomRequest.getSessionId() == null) {
            throw new IllegalArgumentException("Room id, token and session id cannot be empty");
        }

        logger.info("Joining game room with id: {}", roomRequest.getRoomId());
        return gameRoomService.joinGameRoom(roomRequest.getRoomId(), roomRequest.getToken(), roomRequest.getSessionId());
    }

}
