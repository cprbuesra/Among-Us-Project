package org.fhv.amongus.gameroomservice;

import org.fhv.amongus.gameroomservice.DTO.GameRoomDTO;
import org.fhv.amongus.gameroomservice.DTO.PlayerDTO;
import org.fhv.amongus.gameroomservice.model.GameRoom;
import org.fhv.amongus.gameroomservice.model.Player;
import org.fhv.amongus.gameroomservice.model.PlayerInfo;

import java.util.List;
import java.util.stream.Collectors;

public class GameRoomMapper {

    // Convert GameRoom entity to GameRoomDTO
    public static GameRoomDTO toDTO(GameRoom gameRoom) {
        if (gameRoom == null) {
            return null;
        }
        List<PlayerDTO> playerDTOs = gameRoom.getPlayers().stream()
                .map(player -> new PlayerDTO(player.getPlayerId(), player.getUsername(), player.getStatus()))
                .collect(Collectors.toList());

        return new GameRoomDTO(
                gameRoom.getId(),
                gameRoom.getName(),
                gameRoom.isStarted(),
                gameRoom.getCreatedBy(),
                playerDTOs
        );
    }

    // Convert GameRoomDTO to GameRoom entity
    public static GameRoom fromDTO(GameRoomDTO gameRoomDTO) {
        if (gameRoomDTO == null) {
            return null;
        }
        List<Player> players = gameRoomDTO.getPlayers().stream()
                .map(dto -> Player.builder()
                        .playerId(dto.getPlayerId())
                        .username(dto.getUsername())
                        .status(dto.getStatus())
                        .build())
                .collect(Collectors.toList());

        GameRoom gameRoom = new GameRoom(
                gameRoomDTO.getName(),
                gameRoomDTO.getCreatedBy()
        );
        gameRoom.setId(gameRoomDTO.getId());
        gameRoom.setStarted(gameRoomDTO.isStarted());
        gameRoom.setPlayers(players);

        return gameRoom;
    }
}
