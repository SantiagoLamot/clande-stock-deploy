package com.clandestock.backend.venta.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.venta.dto.ResumenProductoVendidoResponseDTO;
import com.clandestock.backend.venta.service.ProductoxVentaService;

@RestController
@RequestMapping("/productoxventa")
public class ProductoxVentaController {
        private final ProductoxVentaService productoxVentaService;
    
        public ProductoxVentaController(ProductoxVentaService productoxVentaService) {
            this.productoxVentaService = productoxVentaService;
        }

        @GetMapping("/caja")
        public ResponseEntity<?> obtenerProductosVendidos() {
            try {
                List<ResumenProductoVendidoResponseDTO> lista = productoxVentaService.obtenerResumenCajaAbierta();
                return ResponseEntity.ok(lista);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }
