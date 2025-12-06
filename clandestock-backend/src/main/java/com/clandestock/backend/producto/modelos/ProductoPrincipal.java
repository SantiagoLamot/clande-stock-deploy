package com.clandestock.backend.producto.modelos;

import java.math.BigDecimal;

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
@Table(name = "ProductoPrincipal_tb")
public class ProductoPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "localID", nullable = false)
    private Local local;

    @Column(nullable = false, length = 100)
    private String nombreProducto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioProducto;

    @Builder.Default
    private Boolean estado = true;

    @Column(nullable = true)
    private int stock;

    @Builder.Default
    private Boolean StockBajo = false;
    
    @Builder.Default
    private Boolean SinStock = false;

    @Column(nullable = true)
    private int aletarStock;

    @Builder.Default
    private Boolean comanda = false;

    @ManyToOne
    @JoinColumn(name = "categoriaID", nullable = false)
    private Categoria categoria;
}
