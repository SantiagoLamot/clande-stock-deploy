package com.clandestock.backend.producto.controller;

import com.clandestock.backend.producto.dto.ActualizarStockRequestDTO;
import com.clandestock.backend.producto.dto.AlertaRequestDTO;
import com.clandestock.backend.producto.dto.ProductoSecundarioRequestDTO;
import com.clandestock.backend.producto.dto.ProductoSecundarioResponseDTO;
import com.clandestock.backend.producto.service.ProductoSecundarioService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos/secundario")
public class ProductoSecundarioController {
    private ProductoSecundarioService productoSecundarioService;

    public ProductoSecundarioController(ProductoSecundarioService pss) {
        this.productoSecundarioService = pss;
    }

    @PostMapping
    public ResponseEntity<?> guardarProductoSecundario(@RequestBody ProductoSecundarioRequestDTO dto) {
        try {
            ProductoSecundarioResponseDTO response = productoSecundarioService.guardarProductoSecundario(dto);
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
            List<ProductoSecundarioResponseDTO> response = productoSecundarioService.obtenerTodosDTO();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoSecundario(@PathVariable String id) {
        try {
            ProductoSecundarioResponseDTO responseDTO = productoSecundarioService.obtenerProductoSecundarioPorId(id);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarProductoSecundario(@RequestBody ProductoSecundarioRequestDTO dto) {
        try {
            ProductoSecundarioResponseDTO responseDTO = productoSecundarioService.actualizarProductoSecundario(dto);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/baja/{id}")
    public ResponseEntity<?> bajaProductoSecundario(@PathVariable String id) {
        try {
            ProductoSecundarioResponseDTO responseDTO = productoSecundarioService.bajaProductoSecundario(id);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/alta/{id}")
    public ResponseEntity<?> altaProductoSecundario(@PathVariable String id) {
        try {
            ProductoSecundarioResponseDTO responseDTO = productoSecundarioService.altaProductoSecundario(id);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/stockbajo/{id}")
    public void stockBajo(@PathVariable String id) {
        try {
            productoSecundarioService.updateStockBajo(Long.parseLong(id));
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
            productoSecundarioService.updateSinStock(Long.parseLong(id));
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
            productoSecundarioService.actualizarStock(dto);
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
            productoSecundarioService.desactivarAlerta(id);
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
            productoSecundarioService.incrementarStock(id);
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
            productoSecundarioService.cargarAlerta(id, dto);
            ResponseEntity.ok();
        } catch (RuntimeException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
