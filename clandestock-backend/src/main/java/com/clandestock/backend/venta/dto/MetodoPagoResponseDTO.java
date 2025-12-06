package com.clandestock.backend.venta.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MetodoPagoResponseDTO(
        String id,
        @NotBlank
        String nombre_metodo_pago,
        String incremento,
        String descuento,
        String estado,
        @NotBlank
        String local_id
) {
}
