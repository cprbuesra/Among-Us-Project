package org.fhv.amongus.chat.chat.model;

import java.util.List;
import java.util.stream.Collectors;

public class ChatMessageMapper {

    // Convert Entity to DTO
    public static ChatMessageDTO toDTO(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getSender(),
                chatMessage.getRoomId()
        );
    }

    // Convert DTO to Entity
    public static ChatMessage fromDTO(ChatMessageDTO chatMessageDTO) {
        if (chatMessageDTO == null) {
            return null;
        }
        return new ChatMessage(
                chatMessageDTO.getId(),
                chatMessageDTO.getContent(),
                chatMessageDTO.getSender(),
                chatMessageDTO.getRoomId()
        );
    }

    // Convert a list of ChatMessage entities to a list of ChatMessageDTOs
    public static List<ChatMessageDTO> toDTOList(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(ChatMessageMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Convert a list of ChatMessageDTOs to a list of ChatMessage entities
    public static List<ChatMessage> fromDTOList(List<ChatMessageDTO> chatMessageDTOs) {
        return chatMessageDTOs.stream()
                .map(ChatMessageMapper::fromDTO)
                .collect(Collectors.toList());
    }
}
