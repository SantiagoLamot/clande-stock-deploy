package com.clandestock.backend.producto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private Long id;

    @JsonProperty("nombre_categoria")
    private String nombreCategoria;

    @JsonProperty("local_id")
    private Long localId;

    private boolean activo; // para saber si está dada de baja
}

