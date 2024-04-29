package org.fhv.amongus.amongus.gameRoom;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.jwt.JwtTokenRepository;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomRepositoryService {

    private final GameRoomRepository gameRoomRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PlayerRepository playerRepository;
    // ToDo Define MAX_PLAYERS in GameRoom class
    private final int MAX_PLAYERS = 10;

    public List<GameRoom> getGameRooms() {
        return gameRoomRepository.findAll();
    }

    public GameRoom createGameRoom(String roomName) {
        GameRoom gameRoom = new GameRoom(roomName);
        return gameRoomRepository.save(gameRoom);
    }

    public GameRoom joinGameRoom(Long roomId, String token, String sessionId) {
        JwtToken jwtToken = jwtTokenRepository.findByTokenAndSessionId(token, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or session ID"));

        if (jwtToken.getExpirationDate().before(new Date())) {
            throw new IllegalArgumentException("Token has expired");
        }

        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Player player = playerRepository.findByUsername(jwtToken.getPlayer().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (gameRoom.getPlayers().size() >= 10) { // Define MAX_PLAYERS as needed
            throw new IllegalStateException("Room is full");
        }

        gameRoom.getPlayers().add(player);
        player.setGameRoom(gameRoom);
        playerRepository.save(player);
        return gameRoomRepository.save(gameRoom);
    }
}
