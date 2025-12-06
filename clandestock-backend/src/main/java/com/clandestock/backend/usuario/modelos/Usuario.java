package com.clandestock.backend.usuario.modelos;

import java.time.LocalDateTime;
import java.util.List;

import com.clandestock.backend.auth.modelos.Token;
import com.clandestock.backend.venta.modelos.Caja;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "Usuario_tb")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(nullable = false, length = 255)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoUsuarioEnum tipoUsuario;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @Builder.Default
    private Boolean estado = true;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Token> tokens;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Caja> cajas;
}