package com.clandestock.backend.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.producto.modelos.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByLocalId(Long id);

    List<Categoria> findByLocal_NombreLocal(String local);

    List<Categoria> findByActivoTrue();

    List<Categoria> findByLocal_NombreLocalAndActivoTrue(String nombreLocal);

    List<Categoria> findByActivoFalse();
}
