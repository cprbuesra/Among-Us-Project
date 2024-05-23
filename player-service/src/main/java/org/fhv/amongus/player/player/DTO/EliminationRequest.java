package org.fhv.amongus.player.player.DTO;

import lombok.Data;
import org.fhv.amongus.player.player.model.Action;

@Data
public class EliminationRequest {
    private Long playerId;
    private Long targetPlayerId;
    private Action action;

}