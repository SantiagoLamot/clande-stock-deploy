package com.clandestock.backend.usuario.service;

import com.clandestock.backend.usuario.dto.ActualizarContrasenaRequestDTO;
import com.clandestock.backend.usuario.dto.ActualizarContrasenaResponse;
import com.clandestock.backend.usuario.dto.UsuarioRequestDTO;
import com.clandestock.backend.usuario.dto.UsuarioResponseDTO;
import com.clandestock.backend.usuario.modelos.TipoUsuarioEnum;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clandestock.backend.usuario.modelos.Usuario;
import com.clandestock.backend.usuario.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository ur, PasswordEncoder passwordEncoder){
        this.usuarioRepository = ur;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }

    public UsuarioResponseDTO toResponse(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDTO(
                usuario.getId() != null ? usuario.getId().toString() : null,
                usuario.getNombreUsuario(),
                usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().name() : null,
                usuario.getFechaCreacion() != null ? usuario.getFechaCreacion().toString() : null,
                usuario.getEstado() != null ? usuario.getEstado().toString() : null
        );
    }

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;

        return Usuario.builder()
                .nombreUsuario(dto.nombreUsuario())
                .contrasena(dto.contrasena())
                .tipoUsuario(dto.tipoUsuario() != null ? TipoUsuarioEnum.valueOf(dto.tipoUsuario()) : null)
                .estado(dto.estado() != null ? Boolean.valueOf(dto.estado()) : true)
                .build();
    }

    public List<UsuarioResponseDTO> listarModeradoresEspeciales() {
        List<TipoUsuarioEnum> tipos = List.of(
                TipoUsuarioEnum.MODERADOR_TENEDOR_LIBRE,
                TipoUsuarioEnum.MODERADOR_TERMAS,
                TipoUsuarioEnum.MODERADOR_HELADERIA
        );

        List<Usuario> usuarios = usuarioRepository.findByTipoUsuarioIn(tipos);

        return usuarios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO darDeBaja(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuario.setEstado(false);
        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    public UsuarioResponseDTO darDeAlta(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuario.setEstado(true);
        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    public UsuarioResponseDTO editarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Actualizamos solo nombre y tipo
        if (dto.nombreUsuario() != null) {
            usuario.setNombreUsuario(dto.nombreUsuario());
        }
        if (dto.tipoUsuario() != null) {
            usuario.setTipoUsuario(TipoUsuarioEnum.valueOf(dto.tipoUsuario()));
        }

        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

    public ActualizarContrasenaResponse actualizarContrasena (String nombreUsuario,
                                                              ActualizarContrasenaRequestDTO request){
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.contrasenaActual(), usuario.getContrasena())){
            throw new IllegalArgumentException("La contraseña actual ingresada es incorrecta o no coincide");
        }

        usuario.setContrasena(passwordEncoder.encode(request.contrasenaNueva()));
        usuarioRepository.save(usuario);

        return new ActualizarContrasenaResponse("La contraseña ha sido actualizada correctamente");
    }
}
