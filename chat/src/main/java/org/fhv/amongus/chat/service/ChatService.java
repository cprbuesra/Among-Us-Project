package org.fhv.amongus.chat.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.model.ChatMessage;
import org.fhv.amongus.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.model.ChatMessageMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepositoryService chatRepositoryService;

    public ChatMessageDTO saveMessage(ChatMessage chatMessage) {
        return chatRepositoryService.saveMessage(chatMessage);
    }
}
