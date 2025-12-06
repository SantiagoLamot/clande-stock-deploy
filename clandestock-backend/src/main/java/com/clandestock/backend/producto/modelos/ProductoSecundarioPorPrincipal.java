package com.clandestock.backend.producto.modelos;

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
@Table(name = "ProdSecxProdPrim_tb")
public class ProductoSecundarioPorPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDProductoPrimario", nullable = false)
    private ProductoPrincipal productoPrimario;

    @ManyToOne
    @JoinColumn(name = "IDProductoSecundario", nullable = false)
    private ProductoSecundario productoSecundario;
}