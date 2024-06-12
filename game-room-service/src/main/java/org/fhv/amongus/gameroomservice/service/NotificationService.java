package org.fhv.amongus.gameroomservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.controller.WebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final WebSocketController webSocketController;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void notifyPlayers(String gameRoomId, String winner) {
        webSocketController.notifyPlayers(gameRoomId, winner);
    }
}
