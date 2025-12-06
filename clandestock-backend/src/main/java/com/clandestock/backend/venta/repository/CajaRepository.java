package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.Caja;
import com.clandestock.backend.venta.modelos.Local;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {

    Optional<Caja> findByLocalAndEstado(Local local, boolean b);
    Optional<Caja> findByLocal_NombreLocalAndEstado(String nombreLocal, boolean estado);
    Optional<Caja> findByEstado(boolean b);
    List<Caja> findByEstadoTrue();
}
