package org.fhv.amongus.amongus.gameRoom.service;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.gameRoom.DTO.GameRoomDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepositoryService gameRoomRepositoryService;

    public GameRoomDTO joinGameRoom(Long roomId, String token, String sessionId) {
        return gameRoomRepositoryService.joinGameRoom(roomId, token, sessionId);
    }

    public List<GameRoomDTO> getGameRooms() {
        return gameRoomRepositoryService.getGameRooms();
    }

    public GameRoomDTO createGameRoom(String roomName, String sessionId) {
        return gameRoomRepositoryService.createGameRoom(roomName, sessionId);
    }

    public GameRoomDTO getGameRoom(Long roomId) {
        return gameRoomRepositoryService.getGameRoom(roomId);
    }

    public GameRoomDTO startGame(Long roomId) {
        return gameRoomRepositoryService.startGame(roomId);
    }

    public void deleteGameRoom(Long roomIdLong, String sessionId) {
        gameRoomRepositoryService.deleteGameRoom(roomIdLong, sessionId);
    }

    public GameRoomDTO leaveGameRoom(Long roomId, String token, String sessionId) {
        return gameRoomRepositoryService.leaveGameRoom(roomId, token, sessionId);
    }
}
