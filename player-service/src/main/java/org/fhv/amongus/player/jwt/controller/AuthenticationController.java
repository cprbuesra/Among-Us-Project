package org.fhv.amongus.player.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.fhv.amongus.player.jwt.DTO.AuthRequest;
import org.fhv.amongus.player.jwt.DTO.AuthResponse;
import org.fhv.amongus.player.jwt.service.JwtTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthenticationController {

    private final JwtTokenService jwtTokenService;

    @PostMapping("/create")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return null;
    }


}
