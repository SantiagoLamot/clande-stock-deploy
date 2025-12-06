package com.clandestock.backend.venta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReporteCajaResponseDTO {
    public Long cajaId;
    public String idLocal;
    public BigDecimal totalGeneral;
    public List<DetalleCajaResponseDTO> detallePorMetodo;
    public LocalDateTime fechaApertura;
    public LocalDateTime fechaCierre;

    public ReporteCajaResponseDTO(Long cajaId, BigDecimal totalGeneral, List<DetalleCajaResponseDTO> detallePorMetodo) {
        this.cajaId = cajaId;
        this.totalGeneral = totalGeneral;
        this.detallePorMetodo = detallePorMetodo;
    }
}
