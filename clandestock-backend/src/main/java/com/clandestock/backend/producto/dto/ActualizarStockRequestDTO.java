package com.clandestock.backend.producto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class ActualizarStockRequestDTO {
    @NotBlank
    @JsonProperty("id_producto")
    public String idProducto;
    
    @NotBlank
    public String stock;
}
