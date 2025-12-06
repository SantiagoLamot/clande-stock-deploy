package com.clandestock.backend.venta.dto;


public record ReporteRequest(
        String descripcion,
        String usuarioEmisor
        //String estado
) {
}
