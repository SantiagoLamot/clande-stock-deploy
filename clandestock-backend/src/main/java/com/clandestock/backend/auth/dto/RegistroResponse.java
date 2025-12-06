package com.clandestock.backend.auth.dto;

import com.clandestock.backend.usuario.modelos.TipoUsuarioEnum;

import java.time.LocalDateTime;

public record RegistroResponse (
        String nombreUsuario,
        String tipoUsuario,
        String fechaCreacion
) {
}
