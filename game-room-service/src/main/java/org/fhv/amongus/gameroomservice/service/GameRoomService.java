package org.fhv.amongus.gameroomservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.fhv.amongus.gameroomservice.DTO.PlayerDTO;
import org.fhv.amongus.gameroomservice.model.GameRoom;
import org.fhv.amongus.gameroomservice.model.Player;
import org.fhv.amongus.gameroomservice.model.PlayerInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
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


    @Transactional
    public void assignRolesToPlayers(String token, String sessionId, Long roomId) throws Exception {

        GameRoom gameRoom = gameRoomRepositoryService.findById(roomId);

        if (gameRoom.isStarted()) {
            return;
        }

        List<Player> players = gameRoom.getPlayers();

        HttpClient client = HttpClient.newBuilder().build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(players);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("localhost:8080/api/player/updatePlayerRoles"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        gameRoom.setStarted(true);
        gameRoomRepositoryService.save(gameRoom);
    }


    public List<Player> getPlayersByRoomId(Long roomId) {
        GameRoom gameRoom = gameRoomRepositoryService.findById(roomId);
        return gameRoom.getPlayers();
    }
}
