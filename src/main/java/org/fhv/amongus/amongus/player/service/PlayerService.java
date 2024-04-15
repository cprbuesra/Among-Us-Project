package org.fhv.amongus.amongus.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fhv.amongus.amongus.boundaries.BoundaryService;
import org.fhv.amongus.amongus.jwt.JwtService;
import org.fhv.amongus.amongus.jwt.JwtToken;
import org.fhv.amongus.amongus.jwt.JwtTokenRepository;
import org.fhv.amongus.amongus.player.DTO.AuthenticationResponse;
import org.fhv.amongus.amongus.player.DTO.RegisterRequest;
import org.fhv.amongus.amongus.player.model.Player;
import org.fhv.amongus.amongus.player.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final JwtTokenRepository jwtTokenRepository;
    private final PlayerRepository _playerRepository;
    private final JwtService jwtService;
    private final BoundaryService boundaryService;
    final int SHIP_WIDTH = 2160;
    final int SHIP_HEIGHT = 1160;



    public AuthenticationResponse savePlayer(RegisterRequest registerRequest) {
        Optional<Player> existingPlayer = _playerRepository.findByUsername(registerRequest.getUsername());
        if (existingPlayer.isPresent()) {
            System.out.println("Player with Username: " + registerRequest.getUsername() + " already exists with ID: " + existingPlayer.get().getPlayerId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player already exists");
        } else {
            System.out.println("Saving player with Username: " + registerRequest.getUsername());
            var player = Player.builder()
                    .username(registerRequest.getUsername())
                    .x(335)
                    .y(20)
                    .build();
            _playerRepository.save(player);


            var jwtToken = jwtService.generateToken(player);
            System.out.println("Generated JWT Token: " + jwtToken);
            var expiryDate = jwtService.extractExpiration(jwtToken);
            String sessionId = UUID.randomUUID().toString();

            JwtToken jwtTokenObj = JwtToken.builder()
                    .token(jwtToken)
                    .player(player)
                    .expirationDate(expiryDate)
                    .sessionId(sessionId)
                    .build();
            jwtTokenRepository.save(jwtTokenObj);
            System.out.println("Saved JWT Token: " + jwtTokenObj);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .sessionId(sessionId)
                    .build();
        }
    }


    public Player movePlayer(String username, String direction, boolean flip) {
        if (username == null || direction == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or direction is null");
        }

        Player player = _playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        int movementSpeed = 2;
        int newX = player.getX();
        int newY = player.getY();

        switch (direction.toUpperCase()) {
            case "UP": newY -= movementSpeed; break;
            case "DOWN": newY += movementSpeed; break;
            case "LEFT": newX -= movementSpeed; break;
            case "RIGHT": newX += movementSpeed; break;
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid direction");
        }

        int absoluteX = newX + SHIP_WIDTH / 2;
        int absoluteY = newY + SHIP_HEIGHT / 2 - 20;

        if (boundaryService.isWithinMovementBoundaries(absoluteX, absoluteY)) {
            player.setX(newX);
            player.setY(newY);
            player.setFlip(flip);
        }
        return _playerRepository.save(player);
    }
}
