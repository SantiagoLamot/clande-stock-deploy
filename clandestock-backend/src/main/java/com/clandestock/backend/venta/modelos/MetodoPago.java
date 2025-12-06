package com.clandestock.backend.venta.modelos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "MetodoPago_tb")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombreMetodoPago;

    @Column
    private Long incremento;

    @Column
    private Long descuento;

    @Builder.Default
    private Boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "localID", nullable = false)
    private Local local;

    @OneToMany(mappedBy = "metodoPago")
    private List<Venta> ventas;
}
