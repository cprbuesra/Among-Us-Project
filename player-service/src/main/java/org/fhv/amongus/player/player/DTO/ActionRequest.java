package org.fhv.amongus.player.player.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.fhv.amongus.player.player.model.Action;
import org.fhv.amongus.player.player.model.Player;

@Data
@Getter
public class ActionRequest {
    private Player playerId;

    private Action action;
    private Player targetPlayerId;
}