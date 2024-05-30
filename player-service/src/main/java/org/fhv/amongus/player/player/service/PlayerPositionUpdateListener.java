package org.fhv.amongus.player.player.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.player.DTO.PlayerPositionUpdateRequest;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.repository.PlayerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PlayerPositionUpdateListener {

    private final PlayerRepository playerRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlayerPositionUpdateListener.class);

    @Bean
    public Queue playerUpdateQueue() {
        logger.info("Rabbit Listener is active");
        return new Queue("playerUpdateQueue", true);
    }

    @RabbitListener(queues = "playerUpdateQueue")
    public void receivePlayerPositionUpdate(@Payload PlayerPositionUpdateRequest request) {
        if (request == null || request.getPlayerId() == null) {
            logger.error("Received update with null request or playerId");
            return;
        }

        logger.info("Received update: {}", request);

        playerRepository.findById(request.getPlayerId())
                .ifPresentOrElse(player -> {
                    player.setX(request.getX());
                    player.setY(request.getY());
                    player.setFlip(request.isFlip());
                    playerRepository.save(player);
                    logger.info("Player position updated: {}", player);
                }, () -> {
                    logger.error("Player not found with ID: {}", request.getPlayerId());
                });
    }
}
