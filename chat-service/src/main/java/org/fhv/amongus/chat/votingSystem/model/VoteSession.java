package org.fhv.amongus.chat.votingSystem.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VoteSession {
    @Id
    @GeneratedValue
    private Long id;
    private String gameRoom;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vote_mapping", joinColumns = @JoinColumn(name = "vote_session_id"))
    @MapKeyColumn(name = "voter_id")
    @Column(name = "target_player_id")
    private Map<String, String> votes = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "player_mapping", joinColumns = @JoinColumn(name = "vote_session_id"))
    @MapKeyColumn(name = "player_id")
    @Column(name = "username")
    private Map<String, String> playerUsernames = new HashMap<>();

    private int skipVotes = 0;
}
