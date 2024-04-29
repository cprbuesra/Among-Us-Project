package org.fhv.amongus.amongus.gameRoom;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepositoryService gameRoomRepositoryService;

    public GameRoom joinGameRoom(Long roomId, String token, String sessionId) {
        return gameRoomRepositoryService.joinGameRoom(roomId, token, sessionId);
    }

    public List<GameRoom> getGameRooms() {
        return gameRoomRepositoryService.getGameRooms();
    }

    public GameRoom createGameRoom(String roomName) {
        return gameRoomRepositoryService.createGameRoom(roomName);
    }
}
