package org.fhv.amongus.chat.chat.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.chat.model.ChatMessage;
import org.fhv.amongus.chat.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor

public class WebSocketController {

    private final ChatService chatService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @MessageMapping("/chat/sendMessage/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageDTO sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String roomId) {
        logger.info("Received message: {} from room {}", chatMessage, roomId);
        return chatService.saveMessage(chatMessage);
    }

    @MessageMapping("/emergencyMeeting/{roomId}")
    @SendTo("/topic/emergencyMeeting/{roomId}")
    public String emergencyMeeting(@DestinationVariable String roomId) {
        logger.info("Emergency Meeting in room {}", roomId);
        return "Emergency Meeting!";
    }


}
