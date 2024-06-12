package org.fhv.amongus.gameroomservice;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.GameEndEvent;
import org.fhv.amongus.gameroomservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameEndEventListener {

    private final NotificationService notificationService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @EventListener
    public void handleGameEndEvent(GameEndEvent event) {
        logger.info("Game ended in room {}", event.getGameRoomId());
        notificationService.notifyPlayers(event.getGameRoomId(), event.getWinner());
    }
}
