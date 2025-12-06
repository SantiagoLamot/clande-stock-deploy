package com.clandestock.backend.venta.controller;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.CajaResponseDTO;
import com.clandestock.backend.venta.dto.ReporteRequest;
import com.clandestock.backend.venta.dto.ReporteResponse;
import com.clandestock.backend.venta.service.CajaService;
import com.clandestock.backend.venta.service.ReporteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reporte")
public class ReporteController {

    private final ReporteService reporteService;
    public ReporteController(ReporteService reporteService){
        this.reporteService = reporteService;
    }

    @PostMapping
    public ResponseEntity<?> cargarReporte (@RequestBody ReporteRequest reporteRequest){
        try {
            ReporteResponse reporteResponse = reporteService.cargarReporte(reporteRequest);
            return ResponseEntity.ok(reporteResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponse> checkReporte(@PathVariable Long id) {
        try {
            ReporteResponse reporteResponse = reporteService.checkReporte(id);
            return ResponseEntity.ok(reporteResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReporte(@PathVariable Long id) {
        try {
            reporteService.deleteReporte(id);
            return ResponseEntity.ok("Reporte eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorialReportes() {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            List<ReporteResponse> reportes = reporteService.obtenerReportesPorContexto(usuario);
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudieron obtener los reportes.");
        }
    }

}
