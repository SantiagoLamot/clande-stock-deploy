package com.clandestock.backend.venta.dto;

import com.clandestock.backend.venta.modelos.TipoVenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NuevaVentaRequestDTO {
    @NotNull
    public TipoVenta tipoVenta;
    @NotBlank
    public String detalleEntrega;
    private Integer numeroMesa;
}
