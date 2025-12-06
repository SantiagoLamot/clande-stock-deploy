package com.clandestock.backend.venta.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MozoResponseDTO {
    private Long id;
    private String nombre;
    private Long localId;
    private String nombreLocal;
}

