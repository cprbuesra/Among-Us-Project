package org.fhv.amongus.amongus.gameRoom.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {
    private String token;
    private String sessionId;
    private String roomName;
}
