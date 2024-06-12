package org.fhv.amongus.movementservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.player.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameRoomServiceClient {

    private final RestTemplate restTemplate;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static void updatePlayerStatus(Long playerId, Long roomId, String status) {
        String url = "http://localhost:8081/api/gameRooms/updatePlayerStatus";

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("playerId", playerId)
                .queryParam("roomId", roomId)
                .queryParam("status", status)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<Void>() {}
        );
    }

    public List<Player> findAllOtherPlayers(Long playerId, String roomId) {
        String url = "http://localhost:8081/api/gameRooms/getAllOtherPlayersByRoom";

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("playerId", playerId)
                .queryParam("roomId", roomId)
                .build()
                .toUri();

        ResponseEntity<List<Player>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Player>>() {}
        );

        List<Player> players = response.getBody();
        logger.info("Other players data: {}", players);
        return players;
    }
}
