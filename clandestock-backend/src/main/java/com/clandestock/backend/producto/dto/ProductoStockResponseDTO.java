package com.clandestock.backend.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoStockResponseDTO {
    String id;
    String nombreProducto;
    String stockDisponible;
    String precio;
    String stockBajo;
    String alertaStockBajo;
    String alertaSinStock;
    String local;
    String tieneSecundarios;
    String comanda;
}
