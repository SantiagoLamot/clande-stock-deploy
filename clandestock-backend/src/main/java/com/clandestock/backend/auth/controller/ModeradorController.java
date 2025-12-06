package com.clandestock.backend.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.auth.dto.RegistroRequest;
import com.clandestock.backend.auth.dto.RegistroResponse;
import com.clandestock.backend.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/moderador")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModeradorController {
    private final AuthService service;

    @PreAuthorize("hasAuthority('ADMIN_GENERAL')")
    @PostMapping("/register")
    public ResponseEntity<?> registroModerador(@RequestBody RegistroRequest mod){
        RegistroResponse modResponse = service.registrarModerador(mod);
        return ResponseEntity.ok(modResponse);
    }
}
