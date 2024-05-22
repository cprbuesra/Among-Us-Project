package org.fhv.amongus.player.player.DTO;

import lombok.Getter;
import lombok.Setter;
import org.fhv.amongus.player.player.model.Action;
@Getter
@Setter
public class EliminationRequest {
    private Long playerId;
    private Long targetPlayerId;
    private Action action;

}