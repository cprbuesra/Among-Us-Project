package org.fhv.amongus.player.player.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

}