package com.clandestock.backend.venta.modelos;

import java.time.LocalDateTime;

import com.clandestock.backend.usuario.modelos.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Reportes_tb")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuarioEmisor", nullable = false)
    private Usuario usuarioEmisor;

    @Column(nullable = false)
    private Boolean estado = false;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = true)
    private LocalDateTime fecha;
}
