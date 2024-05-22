package org.fhv.amongus.player.player.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
public class Player implements UserDetails {

    @Id
    @GeneratedValue
    private Long playerId;
    private String username;
    @Getter
    private int x;
    @Getter
    private int y;
    private boolean flip;
    @Setter
    private Role role;
    public static final double NEAR_DISTANCE = 1.5;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public double calculateDistance(Player otherPlayer) {
        int xDistance = Math.abs(this.x - otherPlayer.getX());
        int yDistance = Math.abs(this.y - otherPlayer.getY());
        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
    }

    public void eliminatePlayer(Player otherPlayer, Action action) {
        if (action == Action.KILL && this.role == Role.IMPOSTER) {
            double distance = calculateDistance(otherPlayer);
            if (distance <= NEAR_DISTANCE) {
                otherPlayer.setRole(Role.GHOST);
            }
        }
    }

    public boolean wouldCollideWith(Player otherPlayer, int newX, int newY) {
        return otherPlayer.getX() == newX && otherPlayer.getY() == newY;
    }
}