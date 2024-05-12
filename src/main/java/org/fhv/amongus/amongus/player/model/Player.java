package org.fhv.amongus.amongus.player.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.fhv.amongus.amongus.gameRoom.model.GameRoom;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
@ToString(exclude = "gameRoom")
public class Player implements UserDetails {

    @Id
    @GeneratedValue
    private Long playerId;
    private String username;
    private int x;
    private int y;
    private boolean flip;
    @ManyToOne
    @JoinColumn(name = "gameRoomId")
    private GameRoom gameRoom;
    private Role role;

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
}
