package org.fhv.amongus.gameroomservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerJoinDTO {
    private String playerId;
    private String username;
    private int x;
    private int y;
    private boolean flip;
    private String role;
    private String status;
}
