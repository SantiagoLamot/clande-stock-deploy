package com.clandestock.backend.venta.modelos;

import java.math.BigDecimal;

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
@Table(name = "ProductoxVenta_tb")
public class ProductoxVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDVenta", nullable = false)
    private Venta venta;


    @Column(nullable = false)
    private Long idProducto;
    
    @Column(nullable = false, length = 100)
    private String nombreProducto;

    @Column(nullable = false)
    private Boolean comanda;

    //precioProducto = Total de producto.precio * cantidad
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioProducto;
}