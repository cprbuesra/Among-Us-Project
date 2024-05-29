package org.fhv.amongus.chat.chat.repository;

import org.fhv.amongus.chat.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByRoomId(Long roomId);
}
