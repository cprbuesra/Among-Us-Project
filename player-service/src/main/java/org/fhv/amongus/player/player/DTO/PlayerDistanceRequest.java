package org.fhv.amongus.player.player.DTO;

import lombok.Getter;
import lombok.Setter;
import org.fhv.amongus.player.player.model.Player;

@Getter
@Setter
public class PlayerDistanceRequest {
    private Player player1;
    private Player player2;

    // getters and setters
}