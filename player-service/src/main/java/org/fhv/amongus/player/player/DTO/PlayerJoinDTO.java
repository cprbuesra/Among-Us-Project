package org.fhv.amongus.player.player.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhv.amongus.player.player.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerJoinDTO {
    private String playerId;
    private String username;
    private int x;
    private int y;
    private boolean flip;
    private Role role;
    private String status;
}
