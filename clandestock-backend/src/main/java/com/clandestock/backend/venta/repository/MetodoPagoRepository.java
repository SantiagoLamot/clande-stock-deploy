package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.modelos.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByLocalId(Long localId);

    List<MetodoPago> findByEstadoTrue();

    List<MetodoPago> findByLocalIdAndEstadoTrue(Long localId);

    List<MetodoPago> findByLocal(Local local);

}
