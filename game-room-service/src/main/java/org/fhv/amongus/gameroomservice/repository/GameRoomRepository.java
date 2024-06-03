package org.fhv.amongus.gameroomservice.repository;

import org.fhv.amongus.gameroomservice.model.GameRoom;
import org.fhv.amongus.gameroomservice.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
    @Query("SELECT p.role FROM GameRoom g JOIN g.players p WHERE p.username = :username")
    String getRoleByUsername(@Param("username") String username);

    @Query("SELECT p FROM GameRoom g JOIN g.players p WHERE g.id = :roomId AND p.playerId <> :playerId")
    List<Player> getAllOtherPlayersByRoom(@Param("playerId") Long playerId, @Param("roomId") Long roomId);
}
