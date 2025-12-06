package com.clandestock.backend.venta.modelos;

import java.util.ArrayList;
import java.util.List;

import com.clandestock.backend.producto.modelos.Categoria;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "Local_tb")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreLocal;

    @OneToMany(mappedBy = "local")
    private List<Caja> cajas;

    @OneToMany(mappedBy = "local")
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "local")
    private List<Venta> ventas;

    @OneToMany(mappedBy = "local")
    private List<Mesa> mesas;

    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mozo> mozos = new ArrayList<>();
}