package org.fhv.amongus.player.player.DTO;

import lombok.Getter;
import lombok.Setter;
import org.fhv.amongus.player.player.model.Action;
import org.fhv.amongus.player.player.model.Player;

@Getter
@Setter
public class ActionRequest {
    private Player player;
    private Action action;
    private Player targetPlayer;
}