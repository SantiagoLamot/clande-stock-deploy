package com.clandestock.backend.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.producto.modelos.ProductoSecundario;


@Repository
public interface ProductoSecundarioRepository extends JpaRepository <ProductoSecundario, Long> {
}
