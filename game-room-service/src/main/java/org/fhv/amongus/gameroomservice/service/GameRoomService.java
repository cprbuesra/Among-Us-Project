package org.fhv.amongus.gameroomservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepositoryService gameRoomRepositoryService;

    public List<GameRoomDTO> getGameRooms() {
        return gameRoomRepositoryService.getGameRooms();
    }

    public GameRoomDTO createGameRoom(String roomName, String sessionId) {
        return gameRoomRepositoryService.createGameRoom(roomName, sessionId);
    }

    public GameRoomDTO joinGameRoom(Long roomId, Long playerId, String username) {
        return gameRoomRepositoryService.joinGameRoom(roomId, playerId, username);
    }

    public GameRoomDTO getGameRoom(Long roomId) {
        return gameRoomRepositoryService.getGameRoom(roomId);
    }

    public void deleteGameRoom(Long roomIdLong, String sessionId, String username) {
        gameRoomRepositoryService.deleteGameRoom(roomIdLong, sessionId, username);
    }

    public GameRoomDTO leaveGameRoom(Long roomId, Long playerId, String username) {
        return gameRoomRepositoryService.leaveGameRoom(roomId, playerId, username);
    }

    public GameRoomDTO startGame(Long roomId) {
        return gameRoomRepositoryService.startGame(roomId);
    }
}
