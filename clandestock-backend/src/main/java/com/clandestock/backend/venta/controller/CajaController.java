package com.clandestock.backend.venta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.venta.dto.AbrirCajaRequestDTO;
import com.clandestock.backend.venta.dto.CajaResponseDTO;
import com.clandestock.backend.venta.dto.ReporteCajaResponseDTO;
import com.clandestock.backend.venta.service.CajaService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/caja")
public class CajaController {
    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<?> abrir(@RequestBody AbrirCajaRequestDTO dto) {
        try {
            CajaResponseDTO response = cajaService.abrir(new BigDecimal(dto.montoApertura));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/cerrar")
    public ResponseEntity<?> cerrar() {
        try {
            CajaResponseDTO response = cajaService.cerrar();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cerradas")
    public ResponseEntity<?> listarCerradas() {
        try {
            List<ReporteCajaResponseDTO> response = cajaService.listarCajasCerradas();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    
    @GetMapping("/detalle")
    public ResponseEntity<?> listarAbiertas() {
        try {
            List<ReporteCajaResponseDTO> response = cajaService.listarCajasAbiertas();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/abierta")
    public ResponseEntity<?> abierta() {
        try {
            List<CajaResponseDTO> response = cajaService.abierta();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
