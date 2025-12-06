package com.clandestock.backend.producto.modelos;

import com.clandestock.backend.venta.modelos.Local;
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
@Table(name = "ProductoSecundario_tb")
public class ProductoSecundario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreProducto;

    @Column(nullable = false)
    private int stock;

    @Builder.Default
    private Boolean estado = true;

    @Builder.Default
    private Boolean StockBajo = false;

    @Builder.Default
    private Boolean SinStock = false;

    @Column(nullable = true)
    private int aletarStock;

    @ManyToOne
    @JoinColumn(name = "localID", nullable = false)
    private Local local;
}