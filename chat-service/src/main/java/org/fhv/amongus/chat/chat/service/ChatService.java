package org.fhv.amongus.chat.chat.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.chat.model.ChatMessage;
import org.fhv.amongus.chat.chat.model.ChatMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepositoryService chatRepositoryService;

    public ChatMessageDTO saveMessage(ChatMessage chatMessage) {
        return chatRepositoryService.saveMessage(chatMessage);
    }

    public List<ChatMessageDTO> getChatHistory(Long roomId) {
        return chatRepositoryService.getChatHistory(roomId);
    }
}
