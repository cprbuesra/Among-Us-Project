package org.fhv.amongus.amongus.gameRoom.repository;


import org.fhv.amongus.amongus.gameRoom.model.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
}
