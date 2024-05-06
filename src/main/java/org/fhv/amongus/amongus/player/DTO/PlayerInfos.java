package org.fhv.amongus.amongus.player.DTO;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfos {

    private String token;
    private String sessionId;
    private String roomId;
}

