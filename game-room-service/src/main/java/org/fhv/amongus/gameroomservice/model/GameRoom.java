package org.fhv.amongus.gameroomservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "game_room_players", joinColumns = @JoinColumn(name = "game_room_id"))
    private List<Player> players = new ArrayList<>();

    private boolean started = false;
    private String createdBy;

    public GameRoom(String name, String sessionId) {
        this.name = name;
        this.createdBy = sessionId;
    }
}
