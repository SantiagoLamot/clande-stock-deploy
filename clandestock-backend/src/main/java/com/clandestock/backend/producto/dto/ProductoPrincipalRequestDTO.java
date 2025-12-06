package com.clandestock.backend.producto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductoPrincipalRequestDTO {
    String id;

    @NotBlank
    String idLocal;

    @NotBlank
    String nombre;

    @NotBlank
    String precio;

    String aletarStockBajo;

    @NotBlank
    String estado;

    String stock;

    String comanda;

    @NotBlank
    String idCategoria;
}
