package org.fhv.amongus.player.player.service;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.player.model.Player;
import org.fhv.amongus.player.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerRepositoryService {

    private final PlayerRepository playerRepository;


    public boolean existsByUsername(String username) {
        return playerRepository.findByUsername(username).isPresent();
    }

    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public void updatePlayerStatus(Long playerId, String status) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.setStatus(status);
        playerRepository.save(player);
    }
}
