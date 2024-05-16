package org.fhv.amongus.gameroomservice.repository;

import org.fhv.amongus.gameroomservice.model.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
}
