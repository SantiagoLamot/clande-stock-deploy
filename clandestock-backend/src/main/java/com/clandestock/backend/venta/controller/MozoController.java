package com.clandestock.backend.venta.controller;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.MesaRequestDTO;
import com.clandestock.backend.venta.dto.MozoRequestDTO;
import com.clandestock.backend.venta.dto.MozoResponseDTO;
import com.clandestock.backend.venta.service.MozoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mozos")
public class MozoController {

    private final MozoService mozoService;
    public MozoController (MozoService ms) {this.mozoService = ms;}

    @PostMapping("/nuevo")
    public ResponseEntity<?> crearMozo(@RequestBody MozoRequestDTO dto) {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MozoResponseDTO response = mozoService.crearMozo(dto, usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear mozo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al crear mozo");
        }
    }

    @GetMapping
    public ResponseEntity<?> listarMozos() {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<MozoResponseDTO> mozos = mozoService.listarMozos(usuario);
            return ResponseEntity.ok(mozos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al listar mozos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al listar mozos");
        }
    }

    @DeleteMapping("/{idMozo}")
    public ResponseEntity<?> eliminarMozo(@PathVariable Long idMozo) {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MozoResponseDTO response = mozoService.eliminarMozo(idMozo, usuario);
            return ResponseEntity.ok("Mozo eliminado: " + response.getNombre() + " del local " + response.getNombreLocal());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar mozo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al eliminar mozo");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@RequestBody MozoRequestDTO request, @PathVariable("id") Long id) {
        try {
            MozoResponseDTO response = mozoService.actualizar(id, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear mozo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al crear mozo");
        }
    }
}
