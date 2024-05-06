package org.fhv.amongus.amongus.gameRoom;

import org.fhv.amongus.amongus.gameRoom.DTO.GameRoomDTO;
import org.fhv.amongus.amongus.gameRoom.model.GameRoom;
import org.fhv.amongus.amongus.player.DTO.PlayerDTO;
import java.util.stream.Collectors;

public class GameRoomMapper {
    public static GameRoomDTO toDTO(GameRoom gameRoom) {
        return new GameRoomDTO(
                gameRoom.getId(),
                gameRoom.getName(),
                gameRoom.isStarted(),
                gameRoom.getCreatedBy(),
                gameRoom.getPlayers()
                        .stream()
                        .map(player -> new PlayerDTO(player.getPlayerId(), player.getUsername()))
                        .collect(Collectors.toList())
        );
    }
}
