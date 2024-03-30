package org.fhv.amongus.amongus.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository _playerRepository;

    public Player savePlayer(Player player) {
        return _playerRepository.save(player);
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
