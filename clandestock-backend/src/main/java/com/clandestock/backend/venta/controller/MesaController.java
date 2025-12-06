package com.clandestock.backend.venta.controller;

import com.clandestock.backend.venta.dto.MesaRequestDTO;
import com.clandestock.backend.venta.dto.MesaResponseDTO;
import com.clandestock.backend.venta.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    @PostMapping
    public ResponseEntity<?> crearMesa(@RequestBody MesaRequestDTO request) {
        try {
            MesaResponseDTO response = mesaService.crearMesa(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Ejemplo: Local no encontrado u otro error de negocio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear la mesa: " + e.getMessage());
        } catch (Exception e) {
            // Cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al crear la mesa");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@RequestBody MesaRequestDTO request, @PathVariable("id") Long id) {
        try {
            MesaResponseDTO response = mesaService.actualizar(request, id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Ejemplo: Local no encontrado u otro error de negocio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear la mesa: " + e.getMessage());
        } catch (Exception e) {
            // Cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al crear la mesa");
        }
    }

    @GetMapping
    public ResponseEntity<?> listarMesasPorUsuario() {
        try {
            List<MesaResponseDTO> mesas = mesaService.listarMesasPorLocal();
            return ResponseEntity.ok(mesas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener las mesas: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener las mesas");
        }
    }
}

