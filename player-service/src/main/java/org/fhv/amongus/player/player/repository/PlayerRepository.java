package org.fhv.amongus.player.player.repository;


import org.fhv.amongus.player.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);

    @Query("SELECT p FROM Player p WHERE p.playerId <> :playerId")
    List<Player> findAllOtherPlayers(@Param("playerId") Long playerId);
}
