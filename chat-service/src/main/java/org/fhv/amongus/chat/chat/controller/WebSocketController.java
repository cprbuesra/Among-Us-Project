package org.fhv.amongus.chat.chat.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.chat.model.ChatMessage;
import org.fhv.amongus.chat.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.chat.service.ChatService;
import org.fhv.amongus.chat.votingSystem.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor

public class WebSocketController {

    private final ChatService chatService;
    private final VotingService votingService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @MessageMapping("/chat/sendMessage/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageDTO sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String roomId) {
        logger.info("Received message: {} from room {}", chatMessage, roomId);
        return chatService.saveMessage(chatMessage);
    }

    @MessageMapping("/emergencyMeeting/{roomId}")
    @SendTo("/topic/emergencyMeeting/{roomId}")
    public ResponseEntity<String> emergencyMeeting(@DestinationVariable String roomId) {
        logger.info("Emergency Meeting in room {}", roomId);
        votingService.initiateVote(roomId);
        logger.info("Voting initiated for game room {}", roomId);
        return ResponseEntity.ok("Emergency Meeting and Voting initiated");
    }

    @MessageMapping("/emergencyMeetingEnd/{roomId}")
    @SendTo("/topic/emergencyMeetingEnd/{roomId}")
    public String emergencyMeetingEnd(@DestinationVariable String roomId) {
        logger.info("Emergency Meeting ended in room {}", roomId);
        votingService.deactivateVoteSession(roomId);
        return "Emergency Meeting ended!";
    }

}
