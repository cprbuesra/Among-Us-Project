package org.fhv.amongus.chat.chat.controller;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/getChatHistory/{roomId}")
    public List<ChatMessageDTO> getChatHistory(@PathVariable String roomId) {
        logger.info("Get chat history for room {}", roomId);
        Long roomIdLong = Long.parseLong(roomId);
        List<ChatMessageDTO> messages = chatService.getChatHistory(roomIdLong);
        logger.info("Found {} messages", messages.size());
        logger.info("Messages: {}", messages);
        return messages;
    }
}
