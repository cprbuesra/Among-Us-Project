package org.fhv.amongus.amongus.gameRoom.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    private Long roomId;
    private String token;
    private String sessionId;
}
