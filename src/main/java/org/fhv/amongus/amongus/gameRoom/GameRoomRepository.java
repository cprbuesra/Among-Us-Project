package org.fhv.amongus.amongus.gameRoom;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
}
