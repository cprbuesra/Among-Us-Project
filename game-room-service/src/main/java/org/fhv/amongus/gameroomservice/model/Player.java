package org.fhv.amongus.gameroomservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Embeddable
public class Player {

    private Long playerId;
    private String username;
    private String role;
    private String status;

    public Player(Long playerId, String username, String alive) {
        this.playerId = playerId;
        this.username = username;
        this.status = alive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(playerId, player.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }

}

