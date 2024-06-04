package org.fhv.amongus.movementservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KillPlayer {
    private String roomId;
    private String targetPlayerId;
}
