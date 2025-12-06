package com.clandestock.backend.venta.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mozo_tb")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mozo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // Relación con Local: muchos mozos pueden pertenecer a un mismo local
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;
}
