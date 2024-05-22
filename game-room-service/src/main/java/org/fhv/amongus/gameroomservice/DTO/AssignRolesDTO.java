package org.fhv.amongus.gameroomservice.DTO;

import lombok.AllArgsConstructor;
import org.fhv.amongus.gameroomservice.model.Player;

import java.util.List;


import lombok.Data;

@Data
@AllArgsConstructor

public class AssignRolesDTO{

        List<Player> players;
}
