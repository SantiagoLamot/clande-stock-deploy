package com.clandestock.backend.venta.dto;

public record ReporteResponse(
        Long id,
        String descripcion,
        String usuarioEmisor,
        String estado,
        String fecha
) {}
