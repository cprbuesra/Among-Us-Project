package org.fhv.amongus.gameroomservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Embeddable
public class Player {

    private Long playerId;
    private String username;
    private String role;

    public Player(Long playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }
}

