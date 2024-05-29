package org.fhv.amongus.chat.votingSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    private String gameRoom;
    private String voterId;
    private String voterUsername;
    private String targetPlayerId;
    private String targetPlayerUsername;
}
