package com.clandestock.backend.auth.service;

import com.clandestock.backend.usuario.modelos.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration-refresh}")
    private long refreshExpiration;

    public String generateToken(final Usuario usuario) {
        return buildToken(usuario, jwtExpiration);
    }

    public String generateRefreshToken(final Usuario usuario) {
        return buildToken(usuario, refreshExpiration);
    }

    private String buildToken(final Usuario usuario, final long expiration) {
        return Jwts.builder()
                .setId(usuario.getId().toString())
                .setClaims(Map.of(
                        "name", usuario.getNombreUsuario(),
                        "tipoUsuario", usuario.getTipoUsuario().name()
                        ))
                .setSubject(usuario.getNombreUsuario())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();

    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, Usuario user) {
        final String username = extractUsername(token);
        return (username.equals(user.getNombreUsuario())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
