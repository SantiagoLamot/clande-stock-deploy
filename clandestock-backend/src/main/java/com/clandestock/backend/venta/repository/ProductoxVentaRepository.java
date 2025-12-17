package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.ProductoxVenta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoxVentaRepository extends JpaRepository<ProductoxVenta, Long> {
    @Query("SELECT p FROM ProductoxVenta p WHERE p.venta.caja.id = :cajaId")
    List<ProductoxVenta> findByCajaId(Long cajaId);
}
