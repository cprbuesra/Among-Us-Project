package org.fhv.amongus.amongus.gameRoom;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhv.amongus.amongus.player.model.Player;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GameRoom {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy = "gameRoom")
    private List<Player> players;


    public GameRoom(String roomName) {
        this.name = roomName;
    }


}
