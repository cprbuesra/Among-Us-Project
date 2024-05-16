package org.fhv.amongus.amongus.jwt;


import lombok.RequiredArgsConstructor;
import org.fhv.amongus.amongus.player.model.Player;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtTokeRepositoryService {

    private final JwtTokenRepository jwtTokenRepository;


    public void deleteByPlayer(Player player) {
        jwtTokenRepository.deleteByPlayer(player);
    }

    public void save(JwtToken jwtTokenObj) {
        jwtTokenRepository.save(jwtTokenObj);
    }

    public Optional<JwtToken> findByPlayer(Player player) {
        return jwtTokenRepository.findByPlayer(player);
    }
}
