package com.clandestock.backend.venta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.venta.modelos.Caja;
import com.clandestock.backend.venta.modelos.Mesa;
import com.clandestock.backend.venta.modelos.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {
        boolean existsByCajaAndEstadoPago(Caja caja, Boolean estadoPago);

        boolean existsByCajaAndFechaCierreIsNull(Caja caja);

        @Query("SELECT v.caja.id, " +
                        "       v.metodoPago.nombreMetodoPago, " +
                        "       SUM(v.precioTotalConMetodoDePago), " +
                        "       v.caja.fechaApertura, " +
                        "       v.caja.fechaCierre, " +
                        "       v.caja.local.id " +
                        "FROM Venta v " +
                        "WHERE v.caja.estado = false " +
                        "GROUP BY v.caja.id, v.metodoPago.nombreMetodoPago, v.caja.fechaApertura, v.caja.fechaCierre, v.caja.local.nombreLocal")
        List<Object[]> obtenerTotalesPorCajaYMetodo();

        @Query("SELECT v.caja.id, " +
                        "       v.metodoPago.nombreMetodoPago, " +
                        "       SUM(v.precioTotalConMetodoDePago), " +
                        "       v.caja.fechaApertura, " +
                        "       v.caja.fechaCierre, " +
                        "       v.caja.local.id " +
                        "FROM Venta v " +
                        "WHERE v.caja.estado = true " +
                        "GROUP BY v.caja.id, v.metodoPago.nombreMetodoPago, v.caja.fechaApertura, v.caja.fechaCierre, v.caja.local.id, v.caja.local.nombreLocal")
        List<Object[]> obtenerTotalesPorCajaAbiertaYMetodo();

        @Query("SELECT v.caja.id, v.metodoPago.nombreMetodoPago, SUM(v.precioTotalConMetodoDePago) " +
                        "FROM Venta v " +
                        "WHERE v.caja.estado = true " +
                        "AND v.estadoPago = true " +
                        "AND v.caja.local.nombreLocal = :nombreLocal " +
                        "GROUP BY v.caja.id, v.metodoPago.nombreMetodoPago")
        List<Object[]> obtenerTotalesPorCajaAbiertaYMetodoPorLocal(String nombreLocal);

        List<Venta> findByEstadoPagoIsFalse();

        List<Venta> findByLocal_NombreLocalAndEstadoPagoIsFalse(String local);

        List<Venta> findByEstadoPagoIsTrue();

        List<Venta> findByCajaAndEstadoPagoIsTrue(Caja caja);

        List<Venta> findByCaja_Id(Long idCaja);

}
