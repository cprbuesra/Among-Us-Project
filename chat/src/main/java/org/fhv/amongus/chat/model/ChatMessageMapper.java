package org.fhv.amongus.chat.model;



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
                chatMessage.getType(),
                chatMessage.getTimestamp()
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
                chatMessageDTO.getType(),
                chatMessageDTO.getTimestamp()
        );
    }
}
