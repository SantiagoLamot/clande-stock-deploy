package com.clandestock.backend.venta.modelos;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Mesa_tb", uniqueConstraints = {
@UniqueConstraint(columnNames = { "local_id", "numero_mesa" })
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Mesa {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Integer numeroMesa;

        @Builder.Default
        private Boolean ocupada = false;

        @ManyToOne
        @JoinColumn(name = "local_id", nullable = false)
        private Local local;

        @OneToMany(mappedBy = "mesa")
        private List<Venta> ventas;
}
