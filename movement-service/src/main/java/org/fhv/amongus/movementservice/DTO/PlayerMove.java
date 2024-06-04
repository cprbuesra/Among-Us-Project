package org.fhv.amongus.movementservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMove {

    private Long playerId;
    private String direction;
    private int positionX;
    private int positionY;
    private boolean flip;
    private String roomId;
    private String sessionId;
    private String token;
    private String username;
    private String status;
}
