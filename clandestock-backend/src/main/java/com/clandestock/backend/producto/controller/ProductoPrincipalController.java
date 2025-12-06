package com.clandestock.backend.producto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.producto.dto.ActualizarStockRequestDTO;
import com.clandestock.backend.producto.dto.AlertaRequestDTO;
import com.clandestock.backend.producto.dto.ProductoPrincipalRequestDTO;
import com.clandestock.backend.producto.dto.ProductoPrincipalResponseDTO;
import com.clandestock.backend.producto.service.ProductoPrincipalService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/producto/principal")
public class ProductoPrincipalController {
    private ProductoPrincipalService productoPrincipalService;

    public ProductoPrincipalController(ProductoPrincipalService pps) {
        this.productoPrincipalService = pps;
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody ProductoPrincipalRequestDTO dto) {
        try {
            ProductoPrincipalResponseDTO response = productoPrincipalService.guardar(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable String id) {
        try {
            ProductoPrincipalResponseDTO response = productoPrincipalService.obtenerPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizar(@RequestBody ProductoPrincipalRequestDTO dto) {
        try {
            ProductoPrincipalResponseDTO response = productoPrincipalService.actualizar(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<ProductoPrincipalResponseDTO> response = productoPrincipalService.obtenerTodos();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/stockbajo/{id}")
    public void stockBajo(@PathVariable String id) {
        try {
            productoPrincipalService.updateStockBajo(Long.parseLong(id));
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/sinstock/{id}")
    public void sinStock(@PathVariable String id) {
        try {
            productoPrincipalService.updateSinStock(Long.parseLong(id));
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN_GENERAL')")
    @PostMapping("/stock")
    public void actualizarStock(@RequestBody ActualizarStockRequestDTO dto) {
        try {
            productoPrincipalService.actualizarStock(dto);
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/desactivarAlerta/{id}")
    public void desactivarAlerta(@PathVariable String id) {
        try {
            productoPrincipalService.desactivarAlerta(id);
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/incrementar/stock/{id}")
    public void incrementarStock(@PathVariable String id) {
        try {
            productoPrincipalService.incrementarStock(id);
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/alertar/{id}")
    public void insertarAlerta(@PathVariable String id,@RequestBody AlertaRequestDTO dto) {
        try {
            productoPrincipalService.insertarAlerta(id, dto);
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
