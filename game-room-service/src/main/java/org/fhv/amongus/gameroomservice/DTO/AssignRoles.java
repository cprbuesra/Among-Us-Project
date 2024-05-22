package org.fhv.amongus.gameroomservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AssignRoles {

    private String token;
    private String sessionId;
    private Long roomId;
}
