package org.fhv.amongus.chat.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.model.ChatMessage;
import org.fhv.amongus.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/topic/{roomId}")
    public ChatMessageDTO sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String roomId) {
        return chatService.saveMessage(chatMessage, roomId);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String roomId) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatService.saveMessage(chatMessage, roomId);
    }
}
