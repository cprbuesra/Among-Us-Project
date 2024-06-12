package org.fhv.amongus.player.player.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfo {

    private Long playerId;
    private String username;
    private String role;
    private String status;
}
