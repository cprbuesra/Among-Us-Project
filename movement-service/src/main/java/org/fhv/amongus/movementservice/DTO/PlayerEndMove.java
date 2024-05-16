package org.fhv.amongus.movementservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEndMove {
    private String username;
    private String roomId;
    private String sessionId;
}
