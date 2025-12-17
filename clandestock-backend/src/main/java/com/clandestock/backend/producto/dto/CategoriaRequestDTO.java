package com.clandestock.backend.producto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    Long id;
    
    @NotBlank
    String nombreCategoria;

    @NotBlank
    String localId;


}
