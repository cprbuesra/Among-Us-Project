package org.fhv.amongus.amongus.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class PlayerService {

    private final JwtTokenRepository jwtTokenRepository;
    private final PlayerRepository _playerRepository;
    private final JwtService jwtService;


    public AuthenticationResponse savePlayer(RegisterRequest registerRequest) {
        Optional<Player> existingPlayer = _playerRepository.findByUsername(registerRequest.getUsername());
        if (existingPlayer.isPresent()) {
            System.out.println("Player with Username: " + registerRequest.getUsername() + " already exists with ID: " + existingPlayer.get().getPlayerId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player already exists");
        } else {
            System.out.println("Saving player with Username: " + registerRequest.getUsername());
            var player = Player.builder()
                    .username(registerRequest.getUsername())
                    .x(0)
                    .y(0)
                    .build();
            _playerRepository.save(player);


            var jwtToken = jwtService.generateToken(player);
            var expiryDate = jwtService.extractExpiration(jwtToken);
            String sessionId = UUID.randomUUID().toString();

            JwtToken jwtTokenObj = JwtToken.builder()
                    .token(jwtToken)
                    .player(player)
                    .expirationDate(expiryDate)
                    .sessionId(sessionId)
                    .build();
            jwtTokenRepository.save(jwtTokenObj);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .sessionId(sessionId)
                    .build();
        }
    }

    public Player movePlayer(String username, String direction) {
        if (username == null || direction == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or direction is null");
        }

        Player player = _playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        final int movementSpeed = 30;

        switch (direction.toUpperCase()) {
            case "UP": player.setY(player.getY() - movementSpeed); break;
            case "DOWN": player.setY(player.getY() + movementSpeed); break;
            case "LEFT": player.setX(player.getX() - movementSpeed); break;
            case "RIGHT": player.setX(player.getX() + movementSpeed); break;
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid direction");
        }
        return _playerRepository.save(player);
    }
}
