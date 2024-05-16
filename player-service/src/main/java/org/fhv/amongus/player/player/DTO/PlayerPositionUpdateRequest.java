package org.fhv.amongus.player.player.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPositionUpdateRequest {

    private Long playerId;
    private int x;
    private int y;
    private boolean flip;
}
