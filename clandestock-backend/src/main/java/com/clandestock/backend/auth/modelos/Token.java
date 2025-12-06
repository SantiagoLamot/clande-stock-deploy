package com.clandestock.backend.auth.modelos;

import com.clandestock.backend.usuario.modelos.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity (name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "Token_tb")
public class Token {

    public enum TokenType {
        BEARER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    private String refreshToken;

    @Builder.Default
    private Boolean isRevoked = false;

    @Builder.Default
    private Boolean isExpired = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;
}