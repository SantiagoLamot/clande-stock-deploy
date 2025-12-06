package com.clandestock.backend.venta.dto;

import jakarta.validation.constraints.NotBlank;

public class AbrirCajaRequestDTO {
    @NotBlank
    public String montoApertura;
}
