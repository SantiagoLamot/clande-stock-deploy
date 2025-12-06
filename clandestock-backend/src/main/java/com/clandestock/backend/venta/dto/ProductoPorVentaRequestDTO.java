package com.clandestock.backend.venta.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductoPorVentaRequestDTO {
    @NotBlank
    public String idVenta;
    @NotBlank
    public Long idProducto;
}
