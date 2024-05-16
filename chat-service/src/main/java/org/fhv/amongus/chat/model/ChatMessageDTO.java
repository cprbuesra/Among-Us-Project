package org.fhv.amongus.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private String content;
    private String sender;
    private ChatMessage.MessageType type;
    private LocalDateTime timestamp;
}
