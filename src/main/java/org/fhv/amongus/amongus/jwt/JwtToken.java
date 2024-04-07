package org.fhv.amongus.amongus.jwt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fhv.amongus.amongus.player.model.Player;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JwtToken   {

    @Id
    @GeneratedValue
    private Long tokenId;
    private String token;
    private String sessionId;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private Date expirationDate;
}
