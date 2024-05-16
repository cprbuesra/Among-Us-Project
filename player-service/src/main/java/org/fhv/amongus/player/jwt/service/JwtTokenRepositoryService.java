package org.fhv.amongus.player.jwt.service;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.model.JwtToken;
import org.fhv.amongus.player.jwt.repository.JwtTokenRepository;
import org.fhv.amongus.player.player.model.Player;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtTokenRepositoryService {

    private final JwtTokenRepository jwtTokenRepository;


    public void deleteByPlayer(Player player) {
        jwtTokenRepository.deleteByPlayer(player);
    }

    public void saveToken(JwtToken jwtTokenObj) {
        jwtTokenRepository.save(jwtTokenObj);
    }

    public Optional<JwtToken> findByPlayer(Player player) {
        return jwtTokenRepository.findByPlayer(player);
    }
}
