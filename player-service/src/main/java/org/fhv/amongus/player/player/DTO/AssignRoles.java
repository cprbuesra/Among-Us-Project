package org.fhv.amongus.player.player.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoles {
    private String token;
    private String sessionId;
}
