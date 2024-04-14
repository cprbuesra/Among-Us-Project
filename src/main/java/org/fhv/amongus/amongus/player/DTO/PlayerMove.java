package org.fhv.amongus.amongus.player.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMove {
    private String direction;
    private boolean flip;
    private String token;
    private String sessionId;
}

