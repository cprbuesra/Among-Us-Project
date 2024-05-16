package org.fhv.amongus.player.player.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhv.amongus.player.player.model.Player;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRolesDTO {

    private String sessionId;
    List<Player> players;
}
