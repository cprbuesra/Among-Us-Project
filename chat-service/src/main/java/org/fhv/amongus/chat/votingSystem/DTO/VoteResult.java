package org.fhv.amongus.chat.votingSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteResult {
    private String mostVotedPlayerId;
    private String mostVotedPlayerUsername;
    private String mostVotedPlayerRole;
    private Map<String, Long> voteCount;
    private String status;
}
