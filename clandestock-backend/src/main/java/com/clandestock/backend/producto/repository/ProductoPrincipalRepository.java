package com.clandestock.backend.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.producto.modelos.ProductoPrincipal;

@Repository
public interface ProductoPrincipalRepository extends JpaRepository<ProductoPrincipal, Long> {

    Long countByCategoriaId(Long categortiaId);

    List<ProductoPrincipal> findByLocalId(long id);

    List<ProductoPrincipal> findByLocal_NombreLocal(String nombreLocal);

    List<ProductoPrincipal> findByCategoriaId(Long categoriaID);
}
