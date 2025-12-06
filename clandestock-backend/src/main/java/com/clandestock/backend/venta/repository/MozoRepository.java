package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.modelos.Mozo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MozoRepository extends JpaRepository<Mozo, Long> {
    List<Mozo> findByLocal_NombreLocal(String nombreLocal);

    Optional<Mozo> findByIdAndLocal(Long id, Local local);
}
