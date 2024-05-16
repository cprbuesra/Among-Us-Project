package org.fhv.amongus.amongus.gameRoom.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhv.amongus.amongus.player.DTO.PlayerDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomDTO {

        private Long id;
        private String name;
        private boolean started;
        private String createdBy;
        private List<PlayerDTO> players;
}
