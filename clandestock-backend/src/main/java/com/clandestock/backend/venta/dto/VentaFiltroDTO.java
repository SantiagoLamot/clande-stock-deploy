package com.clandestock.backend.venta.dto;

import java.time.LocalDateTime;

public record VentaFiltroDTO(
        String usuarioId,
        String metodoPagoId,
        String localId,
        String cajaId,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
) {
}
