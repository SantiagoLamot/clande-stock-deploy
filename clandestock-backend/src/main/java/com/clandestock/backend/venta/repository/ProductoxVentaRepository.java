package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.ProductoxVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoxVentaRepository extends JpaRepository<ProductoxVenta, Long> {
}
