package org.fhv.amongus.gameroomservice.repository;

import org.fhv.amongus.gameroomservice.model.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
    @Query("SELECT p.role FROM GameRoom g JOIN g.players p WHERE p.username = :username")
    String getRoleByUsername(@Param("username") String username);
}
