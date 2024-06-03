package org.fhv.amongus.movementservice.service;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.player.DTO.CollisionRequest;
import org.fhv.amongus.player.player.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class PlayerServiceClient {
    private final String playerServiceUrl = "http://localhost:8084";
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceClient.class);


    public Player findPlayerById(Long playerId) {
        String url = String.format("%s/api/player/getPlayerById/%d", playerServiceUrl, playerId);
        logger.info("Requesting URL: {}", url);

        ResponseEntity<Player> response = restTemplate.getForEntity(url, Player.class);
        Player player = response.getBody();
        logger.info("Player data: {}", player);
        return player;
    }

        /*String url = String.format("http://localhost:8084/api/player/status, request);
        logger.info("Requesting URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Player>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Player>>() {}
        );

        List<Player> players = response.getBody();
        logger.info("Other players data: {}", players);
        return players;*/


    public boolean wouldCollideWith(Long playerId, Long otherPlayerId) {
        String url = String.format("%s/api/player/wouldCollideWith", playerServiceUrl);
        logger.info("Requesting URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CollisionRequest> entity = new HttpEntity<>(new CollisionRequest(playerId, otherPlayerId), headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Boolean.class
        );
        Boolean wouldCollide = response.getBody();
        logger.info("Would collide: {}", wouldCollide);
        return wouldCollide;
    }
}