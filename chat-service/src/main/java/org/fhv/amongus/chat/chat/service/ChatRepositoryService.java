package org.fhv.amongus.chat.chat.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.chat.chat.model.ChatMessage;
import org.fhv.amongus.chat.chat.model.ChatMessageDTO;
import org.fhv.amongus.chat.chat.model.ChatMessageMapper;
import org.fhv.amongus.chat.chat.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRepositoryService {

    private final ChatRepository chatRepository;

    public ChatMessageDTO saveMessage(ChatMessage chatMessage) {
        ChatMessage savedMessage =  chatRepository.save(chatMessage);
        return ChatMessageMapper.toDTO(savedMessage);
    }


    public List<ChatMessageDTO> getChatHistory(Long roomId) {
        List<ChatMessage> chatMessages = chatRepository.findAllByRoomId(roomId);
        return ChatMessageMapper.toDTOList(chatMessages);
    }
}
