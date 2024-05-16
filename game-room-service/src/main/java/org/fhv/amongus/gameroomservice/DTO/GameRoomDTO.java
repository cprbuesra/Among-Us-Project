package org.fhv.amongus.gameroomservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
