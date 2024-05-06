package org.fhv.amongus.amongus.gameRoom.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhv.amongus.amongus.player.model.Player;

import java.util.ArrayList;
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
    @OneToMany(mappedBy = "gameRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();
    private boolean started = false;
    private String createdBy;

    public GameRoom(String name, String sessionId) {
        this.name = name;
        this.createdBy = sessionId;
        this.players = new ArrayList<>();
    }
}
