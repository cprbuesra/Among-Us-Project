package org.fhv.amongus.amongus.jwt;

import org.fhv.amongus.amongus.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long>{

    Optional<JwtToken> findByTokenAndSessionId(String token, String sessionId);
    void deleteByPlayer(Player player);
}
