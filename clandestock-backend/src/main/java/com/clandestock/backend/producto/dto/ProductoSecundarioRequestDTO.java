package com.clandestock.backend.producto.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductoSecundarioRequestDTO(
        String id,
        @NotBlank
        String nombre_producto,
        @NotBlank
        String stock,
        String estado,
        @NotBlank
        String local,
        String aletarStockBajo
) {
}