package org.fhv.amongus.movementservice.service;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.movementservice.DTO.PlayerPositionUpdateRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovementMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendPlayerPositionUpdate(PlayerPositionUpdateRequest updateRequest) {
        rabbitTemplate.convertAndSend("gameExchange", "player.position.update", updateRequest);
    }
}

