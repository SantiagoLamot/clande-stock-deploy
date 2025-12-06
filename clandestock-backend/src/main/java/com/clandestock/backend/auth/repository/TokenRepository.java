package com.clandestock.backend.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clandestock.backend.auth.modelos.Token;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
        SELECT t FROM tokens t
        WHERE t.usuario.id = :userId AND (t.isExpired = false AND t.isRevoked = false)
    """)
    List<Token> findAllValidTokenByUser(@Param("userId") Long userId);

    Optional<Token> findByToken(String jwt);
}
