package com.clandestock.backend.producto.dto;

import jakarta.validation.constraints.NotBlank;

public record AlertaRequestDTO(
    @NotBlank
    String stockBajo,
    @NotBlank
    String sinStock
) {
}
