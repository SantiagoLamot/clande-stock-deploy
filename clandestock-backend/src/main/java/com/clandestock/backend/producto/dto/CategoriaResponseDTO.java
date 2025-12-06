package com.clandestock.backend.producto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CategoriaResponseDTO {
    public String id;
    
    @JsonProperty("nombre_categoria")
    public String nombreCategoria;
    
    @JsonProperty("local_id")
    public String localID;
}
