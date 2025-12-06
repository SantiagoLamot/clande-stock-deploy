package com.clandestock.backend.producto.repository;

import com.clandestock.backend.producto.modelos.ProductoPrincipal;
import com.clandestock.backend.producto.modelos.ProductoSecundario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.producto.modelos.ProductoSecundarioPorPrincipal;

import java.util.List;

@Repository
public interface ProductoSecundarioPorPrincipalRepository extends JpaRepository<ProductoSecundarioPorPrincipal, Long> {

    boolean existsByProductoPrimarioAndProductoSecundario(ProductoPrincipal principal, ProductoSecundario secundario);

    List<ProductoSecundarioPorPrincipal> findByProductoPrimario_Id(Long idProductoPrincipal);

}
