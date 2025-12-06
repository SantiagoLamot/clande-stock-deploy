package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.modelos.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByLocalIdAndNumeroMesa(Long localId, Integer numeroMesa);

    List<Mesa> findByLocal_NombreLocal(String nombreLocal);
    Optional<Mesa> findByLocal_NombreLocalAndNumeroMesa(String nombreLocal, Integer numeroMesa);

    Optional<Mesa> findByIdAndLocal(Long id, Local local);

}
