package org.fhv.amongus.player.player.DTO;

import lombok.Data;

@Data
public class CollisionRequest {
    private Long playerId;
    private Long otherPlayerId;
    private int newX;
    private int newY;
}
