package com.clandestock.backend.venta.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MesaResponseDTO {
    private Long id;
    private Integer numeroMesa;
    private Boolean ocupada;
    private Long localId;
}

