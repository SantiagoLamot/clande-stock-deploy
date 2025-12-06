package com.clandestock.backend.usuario.controller;

import com.clandestock.backend.usuario.dto.UsuarioRequestDTO;
import com.clandestock.backend.usuario.dto.UsuarioResponseDTO;
import com.clandestock.backend.usuario.repository.UsuarioRepository;
import com.clandestock.backend.usuario.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;
    public UsuarioController (UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> obtenerTodosModeradores() {
        try {
            return usuarioService.listarModeradoresEspeciales();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<?> darDeBaja(@PathVariable Long id) {
        try {
            UsuarioResponseDTO response = usuarioService.darDeBaja(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al dar de baja el usuario");
        }
    }

    @PutMapping("/{id}/alta")
    public ResponseEntity<?> darDeAlta(@PathVariable Long id) {
        try {
            UsuarioResponseDTO response = usuarioService.darDeAlta(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al dar de alta el usuario");
        }
    }

    @PutMapping("/{id}/editar")
    public ResponseEntity<?> editarUsuario(@PathVariable Long id,
                                           @RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.editarUsuario(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al editar el usuario");
        }
    }
}
