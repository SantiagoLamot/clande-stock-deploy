package com.clandestock.backend.producto.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductoSecundarioPorPrincipalRequestDTO(
   String id,
   @NotBlank
   String id_producto_principal,
   @NotBlank
   String id_producto_secundario
) {
}
