package com.clandestock.backend.usuario.dto;

import com.clandestock.backend.usuario.modelos.TipoUsuarioEnum;

public record UsuarioRequestDTO(
        String nombreUsuario,
        String contrasena,
        String tipoUsuario,
        String estado
) {
}
