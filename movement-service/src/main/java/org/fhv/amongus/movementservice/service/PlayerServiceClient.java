package org.fhv.amongus.movementservice.service;

import org.fhv.amongus.player.player.DTO.CollisionRequest;
import org.fhv.amongus.player.player.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PlayerServiceClient {
    private final String playerServiceUrl = "http://localhost:8084"; // Replace with the actual URL (8084) of the PlayerService microservice
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceClient.class);

    public PlayerServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Player findPlayerById(Long playerId) {
        String url = String.format("%s/api/player/getPlayerById/%d", playerServiceUrl, playerId);
        logger.info("Requesting URL: {}", url);

        ResponseEntity<Player> response = restTemplate.getForEntity(url, Player.class);
        Player player = response.getBody();
        logger.info("Player data: {}", player);
        return player;
    }


    public List<Player> findAllOtherPlayers(Long playerId) {
        String url = String.format("%s/api/player/getAllPlayers/%d", playerServiceUrl, playerId);
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
        return players;
    }

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
        /*HttpHeaders headers = new HttpHeaders();
        HttpEntity<CollisionRequest> entity = new HttpEntity<>(new CollisionRequest(playerId, otherPlayerId), headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Boolean.class
        );
        return Boolean.FALSE.equals(response.getBody());*/



}