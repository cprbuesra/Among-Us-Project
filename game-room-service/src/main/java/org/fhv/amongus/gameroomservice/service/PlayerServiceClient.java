package org.fhv.amongus.gameroomservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.gameroomservice.DTO.PlayerJoinDTO;
import org.fhv.amongus.gameroomservice.DTO.PlayerStatusUpdateRequest;
import org.fhv.amongus.gameroomservice.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceClient {

    private final RestTemplate restTemplate;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public void updatePlayerStatus(Long playerId, String status) {
        PlayerStatusUpdateRequest request = new PlayerStatusUpdateRequest(playerId, status);
        restTemplate.put("http://localhost:8084/api/player/status", request);
    }

    public List<PlayerJoinDTO> getAllPlayers(List<Long> playerIDs) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(playerIDs, headers);

        ResponseEntity<List<PlayerJoinDTO>> response = restTemplate.exchange(
                "http://localhost:8084/api/player/getAllPlayersByRoom",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<PlayerJoinDTO>>() {}
        );

        List<PlayerJoinDTO> currentPlayers = response.getBody();
        logger.info("Current players data: {}", currentPlayers);

        return currentPlayers;
    }

}
