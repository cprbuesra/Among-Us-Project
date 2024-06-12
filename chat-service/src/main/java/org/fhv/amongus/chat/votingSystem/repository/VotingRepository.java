package org.fhv.amongus.chat.votingSystem.repository;

import org.fhv.amongus.chat.votingSystem.model.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingRepository extends JpaRepository<VoteSession, Long>{

    VoteSession findByGameRoomAndActive(String gameRoom, boolean active);
}
