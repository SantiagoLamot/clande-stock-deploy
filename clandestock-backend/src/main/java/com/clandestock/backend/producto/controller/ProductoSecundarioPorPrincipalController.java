package com.clandestock.backend.producto.controller;

import com.clandestock.backend.producto.dto.ProductoSecundarioPorPrincipalRequestDTO;
import com.clandestock.backend.producto.dto.ProductoSecundarioPorPrincipalResponseDTO;
import com.clandestock.backend.producto.dto.ProductoSecundarioResponseDTO;
import com.clandestock.backend.producto.service.ProductoSecundarioPorPrincipalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos/relacion")
public class ProductoSecundarioPorPrincipalController {
    private ProductoSecundarioPorPrincipalService productoSecundarioPorPrincipalService;

    public ProductoSecundarioPorPrincipalController(
            ProductoSecundarioPorPrincipalService productoSecundarioPorPrincipalService) {
        this.productoSecundarioPorPrincipalService = productoSecundarioPorPrincipalService;
    }

    @PostMapping
    public ResponseEntity<?> guardarRelacionProductos(@RequestBody ProductoSecundarioPorPrincipalRequestDTO dto) {
        try {
            ProductoSecundarioPorPrincipalResponseDTO responseDTO = productoSecundarioPorPrincipalService
                    .guardarRelacion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

    @GetMapping("{idPrincipal}")
    public ResponseEntity<?> obtenerProductosSecundariosPorPrincipal(@PathVariable Long idPrincipal) {
        try {
            List<ProductoSecundarioResponseDTO> secundarios = productoSecundarioPorPrincipalService
                    .obtenerSecundariosPorPrincipal(idPrincipal);

            return ResponseEntity.ok(secundarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener productos secundarios: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idRelacion}")
    public ResponseEntity<?> eliminarFisicamenteRelacion(@PathVariable Long idRelacion) {
        try {
            productoSecundarioPorPrincipalService.eliminarRelacion(idRelacion);
            return ResponseEntity.ok("Relacion eliminada correctamente. Info: ID - " + idRelacion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la relación: " + e.getMessage());
        }
    }
}
