package com.clandestock.backend.usuario.dto;

public record UsuarioResponseDTO(
        String id,
        String nombreUsuario,
        String tipoUsuario,
        String fechaCreacion,
        String estado
) {
}
