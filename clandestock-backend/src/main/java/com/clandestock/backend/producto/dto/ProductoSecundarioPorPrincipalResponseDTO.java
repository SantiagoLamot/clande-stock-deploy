package com.clandestock.backend.producto.dto;

public record ProductoSecundarioPorPrincipalResponseDTO(
        String id,
        String id_producto_principal,
        String id_producto_secundario
) {
}
