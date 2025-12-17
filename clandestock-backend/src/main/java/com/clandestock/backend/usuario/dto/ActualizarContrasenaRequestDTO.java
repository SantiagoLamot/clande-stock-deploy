package com.clandestock.backend.usuario.dto;

public record ActualizarContrasenaRequestDTO(
   String contrasenaActual,
   String contrasenaNueva
) {
}
