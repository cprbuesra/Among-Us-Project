package org.fhv.amongus.gameroomservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.fhv.amongus.gameroomservice.GameRoomMapper;
import org.fhv.amongus.gameroomservice.model.GameRoom;
import org.fhv.amongus.gameroomservice.model.Player;
import org.fhv.amongus.gameroomservice.model.PlayerInfo;
import org.fhv.amongus.gameroomservice.repository.GameRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRoomRepositoryService {

    private final GameRoomRepository gameRoomRepository;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

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

    public GameRoomDTO joinGameRoom(Long roomId, Long playerId, String username) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        //PlayerInfo playerInfo = new PlayerInfo(playerId, username);
        Player player = Player.builder().playerId(playerId).username(username).build();
        gameRoom.getPlayers().add(player);
        gameRoomRepository.save(gameRoom);

        //TODO: Notify Player Service that player has joined the game room

        return GameRoomMapper.toDTO(gameRoom);
    }

    public GameRoomDTO getGameRoom(Long roomId) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        return GameRoomMapper.toDTO(gameRoom);
    }

    public void deleteGameRoom(Long roomIdLong, String sessionId, String username) {

        GameRoom gameRoom = gameRoomRepository.findById(roomIdLong)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!gameRoom.getCreatedBy().equals(sessionId)) {
            throw new SecurityException("Only the creator can delete the game room");
        }

        gameRoomRepository.delete(gameRoom);
        logger.info("Game room with id: {} deleted by user: {}", roomIdLong, username);
    }

    @Transactional
    public GameRoomDTO leaveGameRoom(Long roomId, Long playerId, String username) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        boolean removed = gameRoom.getPlayers().removeIf(playerInfo -> playerInfo.getPlayerId().equals(playerId));
        if (!removed) {
            logger.info("{}  not found in room: {}", username, roomId);
        }

        gameRoomRepository.save(gameRoom);

        return GameRoomMapper.toDTO(gameRoom);
    }

    @Transactional
    public GameRoomDTO startGame(Long roomId) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        for (Player player : gameRoom.getPlayers()) {
            System.out.println();
        }

        gameRoom.setStarted(true);
        GameRoom updatedRoom = gameRoomRepository.save(gameRoom);

        return GameRoomMapper.toDTO(updatedRoom);
    }

    public GameRoom findById(Long roomId) {
        return gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    public GameRoom save(GameRoom gameRoom) {
        return gameRoomRepository.save(gameRoom);
    }

}
