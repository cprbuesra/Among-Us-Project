package org.fhv.amongus.movementservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPosition {
    private Long playerId;
    private int newPositionX;
    private int newPositionY;
    private boolean flip;
    private String sessionId;
}
