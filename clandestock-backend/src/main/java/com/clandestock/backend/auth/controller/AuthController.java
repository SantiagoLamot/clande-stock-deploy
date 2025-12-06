package com.clandestock.backend.auth.controller;

import com.clandestock.backend.auth.dto.LoginRequest;
import com.clandestock.backend.auth.dto.RegistroRequest;
import com.clandestock.backend.auth.dto.TokenResponse;
import com.clandestock.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registro(@RequestBody final RegistroRequest request) {
        final TokenResponse token = service.registro(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        final TokenResponse response = service.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication) {
        return service.refreshToken(authentication);
    }
}
