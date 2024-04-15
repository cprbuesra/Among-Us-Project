package org.fhv.amongus.amongus.player.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPosition {
    private String token;
    private String sessionId;
    private int newPositionX;
    private int newPositionY;
    private boolean flipX;
}
