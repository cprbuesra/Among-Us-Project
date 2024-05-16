package org.fhv.amongus.gameroomservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private Long roomId;
    private Long playerId;
    private String username;
}
