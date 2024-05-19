package org.fhv.amongus.movementservice.service;

import org.fhv.amongus.player.player.model.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerServiceClient {
    private final String playerServiceUrl = "http://localhost:8080"; // Replace with the actual URL of the PlayerService microservice
    private final RestTemplate restTemplate;

    public PlayerServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Player findPlayerById(Long playerId) {
        return restTemplate.getForObject(playerServiceUrl + "/players/" + playerId, Player.class);
    }

    public List<Player> findAllOtherPlayers(Long playerId) {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(playerServiceUrl + "/players?exclude=" + playerId, Player[].class)));
    }
}