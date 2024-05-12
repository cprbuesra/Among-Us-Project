package org.fhv.amongus.amongus.gameRoom.service;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.gameRoom.DTO.GameRoomDTO;
import org.fhv.amongus.amongus.gameRoom.GameRoomMapper;
import org.fhv.amongus.amongus.gameRoom.repository.GameRoomRepository;
import org.fhv.amongus.amongus.gameRoom.model.GameRoom;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.jwt.JwtTokenRepository;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRoomRepositoryService {

    private final GameRoomRepository gameRoomRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PlayerRepository playerRepository;

    public List<GameRoomDTO> getGameRooms() {
        return gameRoomRepository.findAll()
                .stream()
                .map(GameRoomMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GameRoomDTO createGameRoom(String roomName, String sessionId) {
        GameRoom gameRoom = new GameRoom(roomName, sessionId);
        GameRoom savedRoom = gameRoomRepository.save(gameRoom);
        return GameRoomMapper.toDTO(savedRoom);
    }

    public GameRoomDTO joinGameRoom(Long roomId, String token, String sessionId) {
        JwtToken jwtToken = jwtTokenRepository.findByTokenAndSessionId(token, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or session ID"));

        if (jwtToken.getExpirationDate().before(new Date())) {
            throw new IllegalArgumentException("Token has expired");
        }

        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Player player = playerRepository.findByUsername(jwtToken.getPlayer().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (gameRoom.getPlayers().size() >= 10) {
            throw new IllegalStateException("Room is full");
        }

        gameRoom.getPlayers().add(player);
        player.setGameRoom(gameRoom);
        playerRepository.save(player);

        GameRoom updatedRoom = gameRoomRepository.save(gameRoom);
        return GameRoomMapper.toDTO(updatedRoom);
    }

    public GameRoomDTO getGameRoom(Long roomId) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return GameRoomMapper.toDTO(gameRoom);
    }

    public GameRoomDTO startGame(Long roomId) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        gameRoom.setStarted(true);

        GameRoom updatedRoom = gameRoomRepository.save(gameRoom);

        return GameRoomMapper.toDTO(updatedRoom);
    }

    public void deleteGameRoom(Long roomIdLong, String sessionId){
        GameRoom gameRoom = gameRoomRepository.findById(roomIdLong)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!gameRoom.getCreatedBy().equals(sessionId)) {
            throw new IllegalArgumentException("Only the creator of the room can delete it");
        }

        gameRoomRepository.delete(gameRoom);
    }

    public GameRoomDTO leaveGameRoom(Long roomId, String token, String sessionId) {
        JwtToken jwtToken = jwtTokenRepository.findByTokenAndSessionId(token, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or session ID"));

        if (jwtToken.getExpirationDate().before(new Date())) {
            throw new IllegalArgumentException("Token has expired");
        }

        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Player player = playerRepository.findByUsername(jwtToken.getPlayer().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        gameRoom.getPlayers().remove(player);
        player.setGameRoom(null);
        playerRepository.save(player);

        GameRoom updatedRoom = gameRoomRepository.save(gameRoom);
        return GameRoomMapper.toDTO(updatedRoom);
    }

}
