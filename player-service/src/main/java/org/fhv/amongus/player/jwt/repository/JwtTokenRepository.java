package org.fhv.amongus.player.jwt.repository;


import org.fhv.amongus.player.jwt.model.JwtToken;
import org.fhv.amongus.player.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByTokenAndSessionId(String token, String sessionId);
    void deleteByPlayer(Player player);
    Optional<JwtToken> findByPlayer(Player player);

    Optional<JwtToken> findByToken(String token);
}

