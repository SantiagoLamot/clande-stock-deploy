package com.clandestock.backend.usuario.repository;

import java.util.List;
import java.util.Optional;

import com.clandestock.backend.usuario.modelos.TipoUsuarioEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.usuario.modelos.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String username);
    List<Usuario> findByTipoUsuarioIn(List<TipoUsuarioEnum> tipos);
}
