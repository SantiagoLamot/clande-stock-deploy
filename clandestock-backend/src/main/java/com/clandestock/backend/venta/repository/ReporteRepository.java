package com.clandestock.backend.venta.repository;

import com.clandestock.backend.venta.modelos.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByUsuarioEmisor_Id(Long idUsuario);
}
