package org.fhv.amongus.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fhv.amongus.chat.model.ChatMessage;
import org.fhv.amongus.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.model.ChatMessageMapper;
import org.fhv.amongus.chat.repository.ChatRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRepositoryService {

    private final ChatRepository chatRepository;

    public ChatMessageDTO saveMessage(ChatMessage chatMessage) {

        ChatMessage savedMessage =  chatRepository.save(chatMessage);
        return ChatMessageMapper.toDTO(savedMessage);
    }
}
