package com.clandestock.backend.venta.dto;

import jakarta.validation.constraints.NotBlank;

public record MetodoPagoRequestDTO (
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
