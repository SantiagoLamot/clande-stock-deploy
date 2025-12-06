package com.clandestock.backend.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clandestock.backend.auth.repository.TokenRepository;
import com.clandestock.backend.auth.service.JwtService;
import com.clandestock.backend.usuario.modelos.TipoUsuarioEnum;
import com.clandestock.backend.usuario.modelos.Usuario;
import com.clandestock.backend.usuario.repository.UsuarioRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String nombreUsuario = jwtService.extractUsername(jwt);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (nombreUsuario == null || authentication != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuarioOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        final Usuario usuario = usuarioOpt.get();
        final boolean isTokenValid = jwtService.isTokenValid(jwt, usuario);
        final boolean isTokenActive = tokenRepository.findByToken(jwt)
                .map(token -> !token.getIsExpired() && !token.getIsRevoked())
                .orElse(false);

        if (isTokenValid && isTokenActive) {
            TipoUsuarioEnum tipo = usuario.getTipoUsuario();

            UsuarioContexto contexto = new UsuarioContexto(usuario.getNombreUsuario(), tipo);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    contexto,
                    null,
                    List.of(new SimpleGrantedAuthority(tipo.name())));

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
