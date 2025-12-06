package com.clandestock.backend.venta.modelos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.clandestock.backend.usuario.modelos.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Ventas_tb")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    // agg variable de descuento increment (precio total modificado por metodo de
    // pago)
    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal precioTotalConMetodoDePago;

    @ManyToOne
    @JoinColumn(name = "metodoDePago", nullable = true)
    private MetodoPago metodoPago;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", nullable = true)
    private LocalDateTime fechaCierre;

    @Builder.Default
    private Boolean estadoPago = false;

    @OneToMany(mappedBy = "venta")
    private List<ProductoxVenta> productos;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVenta tipoVenta;

    @Column(nullable = true, length = 200)
    private String detalleEntrega;

    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @ManyToOne
    @JoinColumn(name = "caja_id", nullable = false)
    private Caja caja;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = true) // puede ser null si la venta no está asociada a una mesa
    private Mesa mesa;

    @Column(nullable = true) // puede ser null si la venta no está asociada a una mesa
    private Integer numeroMesa;
}