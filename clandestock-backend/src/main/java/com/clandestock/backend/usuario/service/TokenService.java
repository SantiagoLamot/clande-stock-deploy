package com.clandestock.backend.usuario.service;

import org.springframework.stereotype.Service;

import com.clandestock.backend.auth.repository.TokenRepository;

@Service
public class TokenService {
    private TokenRepository tokenRepository;

    public TokenService(TokenRepository tr) {
        this.tokenRepository = tr;
    }
}
