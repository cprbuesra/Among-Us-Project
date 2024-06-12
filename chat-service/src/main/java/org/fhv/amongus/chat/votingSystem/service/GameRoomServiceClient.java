package org.fhv.amongus.chat.votingSystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GameRoomServiceClient {
    private final RestTemplate restTemplate;

    public void checkWinCondition(String gameRoom) {
        String url = "http://localhost:8081/api/gameRooms/checkWinCondition/" + gameRoom;
        restTemplate.postForObject(url, null, Void.class);
    }
}
