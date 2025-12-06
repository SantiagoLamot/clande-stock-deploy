package com.clandestock.backend.producto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.producto.dto.ProductoStockResponseDTO;
import com.clandestock.backend.producto.service.ProductoStockService;

@RestController
@RequestMapping("/productos")
public class ProductoStockController {
    private ProductoStockService productoStockService;

    public ProductoStockController(ProductoStockService pss) {
        this.productoStockService = pss;
    }

    @GetMapping("/stock")
    public ResponseEntity<?> obtenerConStock(
            @RequestParam(required = false) Long categoriaID) {
        try {
            List<ProductoStockResponseDTO> response = productoStockService.productosConStock(categoriaID);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/principal/alertas")
    public ResponseEntity<?> obtenerAlertasPrimarios() {
        try {
            return ResponseEntity.ok(productoStockService.productosPrimariosEnAlerta());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/secundario/alertas")
    public ResponseEntity<?> obtenerAlertasSecundarios() {
        try {
            return ResponseEntity.ok(productoStockService.productosSecundariosEnAlerta());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
