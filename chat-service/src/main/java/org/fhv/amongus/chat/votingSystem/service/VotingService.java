package org.fhv.amongus.chat.votingSystem.service;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.votingSystem.DTO.VoteResult;
import org.fhv.amongus.chat.votingSystem.model.VoteSession;
import org.fhv.amongus.chat.votingSystem.repository.VotingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final VotingRepository votingRepository;
    private final GameRoomServiceClient gameRoomServiceClient;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void initiateVote(String gameRoom) {
        // Close any existing active voting sessions for this game room
        VoteSession activeSession = votingRepository.findByGameRoomAndActive(gameRoom, true);
        if (activeSession != null) {
            activeSession.setActive(false);
            votingRepository.save(activeSession);
        }

        // Start a new voting session
        VoteSession voteSession = new VoteSession();
        voteSession.setGameRoom(gameRoom);
        voteSession.setActive(true);  // Mark this session as active
        logger.info("Voting initiated for game room {} and is {}", gameRoom, voteSession.isActive());
        votingRepository.save(voteSession);
    }

    public void castVote(String gameRoom, String voterId, String targetPlayerId, String voterUsername, String targetPlayerUsername, String targetPlayerRole) {
        VoteSession voteSession = votingRepository.findByGameRoomAndActive(gameRoom, true);
        logger.info("this is the game room: {}", gameRoom);
        logger.info("this is the vote session: {}", voteSession);

        if (voteSession == null) {
            logger.warn("No active voting session found in game room {}", gameRoom);
            return;
        }

        if (targetPlayerId == null) {
            voteSession.getVotes().remove(voterId);
            logger.info("{} retracted their vote", voterUsername);
        } else if (targetPlayerId.equals("skip")) {
            voteSession.getVotes().put(voterId, targetPlayerId);
            voteSession.setSkipVotes(voteSession.getSkipVotes() + 1);
            logger.info("{} wants to skip the vote in game room {}", voterUsername, gameRoom);
        } else {
            voteSession.getVotes().put(voterId, targetPlayerId);
            if (!targetPlayerUsername.isEmpty()) {
                voteSession.getPlayerUsernames().put(targetPlayerId, targetPlayerUsername);
            }
            if (!targetPlayerRole.isEmpty()) {
                voteSession.getPlayerRoles().put(targetPlayerId, targetPlayerRole);
            }
            logger.info("{} wants to vote out {} who has the role {} in game room {}", voterUsername, targetPlayerUsername, targetPlayerRole, gameRoom);
        }
        votingRepository.save(voteSession);
    }

    public VoteResult getVoteResults(String gameRoom) {
        VoteSession voteSession = votingRepository.findByGameRoomAndActive(gameRoom, true);
        if (voteSession == null) {
            logger.warn("No active voting session found for game room {}", gameRoom);
            return new VoteResult();
        }

        Map<String, Long> voteCounts = voteSession.getVotes().values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long totalVotes = voteCounts.values().stream().mapToLong(Long::longValue).sum();
        long skipVotes = voteCounts.getOrDefault("skip", 0L);

        logger.info("Total votes: {}", totalVotes);
        logger.info("Skip votes: {}", skipVotes);

        VoteResult voteResult = new VoteResult();
        voteResult.setVoteCount(voteCounts);

        if (totalVotes == 0) {
            logger.info("No votes cast, voting skipped");
            voteResult.setStatus("skipped");
            voteResult.setMostVotedPlayerId("none");
            voteResult.setMostVotedPlayerUsername("none");
            voteResult.setMostVotedPlayerRole("none");
        } else if (skipVotes >= totalVotes / 2) {
            logger.info("Voting skipped, no player was voted out");
            voteResult.setStatus("skipped");
            voteResult.setMostVotedPlayerId("none");
            voteResult.setMostVotedPlayerUsername("none");
            voteResult.setMostVotedPlayerRole("none");
        } else {
            long maxVoteCount = Collections.max(voteCounts.values());

            long playersWithMaxVotes = voteCounts.values().stream()
                    .filter(count -> count == maxVoteCount)
                    .count();

            if (playersWithMaxVotes > 1) {
                logger.info("Tie in votes, no player was voted out");
                voteResult.setStatus("tie");
                voteResult.setMostVotedPlayerId("none");
                voteResult.setMostVotedPlayerUsername("none");
                voteResult.setMostVotedPlayerRole("none");
            } else {
                String mostVotedPlayerId = voteCounts.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxVoteCount)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);

                String mostVotedPlayerUsername = voteSession.getPlayerUsernames().get(mostVotedPlayerId);
                String mostVotedPlayerRole = voteSession.getPlayerRoles().get(mostVotedPlayerId);

                logger.info("Player {} ({}) was voted out and he has the role {}", mostVotedPlayerUsername, mostVotedPlayerId, mostVotedPlayerRole);

                voteResult.setStatus("votedOut");
                voteResult.setMostVotedPlayerId(mostVotedPlayerId);
                voteResult.setMostVotedPlayerUsername(mostVotedPlayerUsername);
                voteResult.setMostVotedPlayerRole(mostVotedPlayerRole);
            }
        }
        votingRepository.save(voteSession);
        gameRoomServiceClient.checkWinCondition(gameRoom);
        return voteResult;
    }

    public void deactivateVoteSession(String roomId) {
        VoteSession voteSession = votingRepository.findByGameRoomAndActive(roomId, true);
        if (voteSession != null) {
            voteSession.setActive(false);
            votingRepository.save(voteSession);
        }
    }
}
