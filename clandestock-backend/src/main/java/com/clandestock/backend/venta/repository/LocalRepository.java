package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.Local;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByNombreLocal(String nombre);
}
