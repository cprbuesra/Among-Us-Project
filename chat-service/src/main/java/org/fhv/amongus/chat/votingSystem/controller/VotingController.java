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
        logger.info("Vote initiated");
        return ResponseEntity.ok("Vote initiated");
    }

    @PostMapping("/vote")
    public ResponseEntity<String> castVote(@RequestBody VoteRequest request) {
        votingService.castVote(request.getGameRoom(), request.getVoterId(), request.getTargetPlayerId(), request.getVoterUsername(), request.getTargetPlayerUsername());
        return ResponseEntity.ok("Vote cast");
    }


    @GetMapping("/results/{gameRoom}")
    public ResponseEntity<VoteResult> getVoteResults(@PathVariable String gameRoom) {
        VoteResult result = votingService.getVoteResults(gameRoom);
        return ResponseEntity.ok(result);
    }
}
