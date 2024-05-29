package org.fhv.amongus.gameroomservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.PlayerStatusUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MovementServiceClient {

    private final RestTemplate restTemplate;

    public void updatePlayerStatus(Long playerId, String status) {
        PlayerStatusUpdateRequest request = new PlayerStatusUpdateRequest(playerId, status);
        restTemplate.put("http://localhost:8082/api/movement/status", request);
    }
}
