package org.fhv.amongus.amongus.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlayerService {

    private final PlayerRepository _playerRepository;


    public Player savePlayer(Player player) {
        Optional<Player> existingPlayer = _playerRepository.findByUsername(player.getUsername());
        if (existingPlayer.isPresent()) {
            System.out.println("Player with Username: " + player.getUsername() + " already exists with ID: " + existingPlayer.get().getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player already exists");
        } else {
            System.out.println("Saving player with Username: " + player.getUsername() + " and ID: " + player.getId());
            return _playerRepository.save(player);
        }
    }

    public Player movePlayer(Long userId, String direction) {
        Player player = _playerRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        int movementSpeed = 30;

        switch (direction) {
            case "UP": player.setY(player.getY() - movementSpeed); break;
            case "DOWN": player.setY(player.getY() + movementSpeed); break;
            case "LEFT": player.setX(player.getX() - movementSpeed); break;
            case "RIGHT": player.setX(player.getX() + movementSpeed); break;
        }

        return _playerRepository.save(player);
    }



}
