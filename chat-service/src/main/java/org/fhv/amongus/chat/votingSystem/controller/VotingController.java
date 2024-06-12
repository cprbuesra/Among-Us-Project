package org.fhv.amongus.chat.votingSystem.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.votingSystem.DTO.VoteRequest;
import org.fhv.amongus.chat.votingSystem.DTO.VoteResult;
import org.fhv.amongus.chat.votingSystem.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voting")
public class VotingController {

    private final VotingService votingService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/initiateVoting/{gameRoom}")
    public ResponseEntity<String> initiateVote( @PathVariable String gameRoom) {
        votingService.initiateVote(gameRoom);
        logger.info("Voting initiated for game room {}", gameRoom);
        return ResponseEntity.ok("Vote initiated");
    }

    @PostMapping("/vote")
    public ResponseEntity<String> castVote(@RequestBody VoteRequest request) {
        votingService.castVote(request.getGameRoom(), request.getVoterId(), request.getTargetPlayerId(), request.getVoterUsername(), request.getTargetPlayerUsername(), request.getTargetPlayerRole());
        return ResponseEntity.ok("Vote cast");
    }


    @GetMapping("/results/{roomId}")
    public ResponseEntity<VoteResult> getVoteResults(@PathVariable String roomId) {
        logger.info("Getting vote results for game room {}", roomId);
        VoteResult result = votingService.getVoteResults(roomId);
        if (result.getStatus() == null) {
            logger.warn("No active voting session found for game room {}", roomId);
            return ResponseEntity.notFound().build();
        }
        logger.info("Vote results for game room {}: {}", roomId, result);
        return ResponseEntity.ok(result);
    }
}
