package org.fhv.amongus.amongus.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPosition {
    private Long playerId;
    private int newPositionX;
    private int newPositionY;
}
