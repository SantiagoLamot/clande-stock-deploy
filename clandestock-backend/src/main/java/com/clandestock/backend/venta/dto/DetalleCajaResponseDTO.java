package com.clandestock.backend.venta.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class DetalleCajaResponseDTO {
    public Long cajaId;
    public String metodoPago;
    public BigDecimal totalCobrado;
}