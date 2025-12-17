package com.clandestock.backend.venta.controller;

import com.clandestock.backend.venta.dto.VentaFiltroDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.venta.dto.NuevaVentaRequestDTO;
import com.clandestock.backend.venta.dto.ProductoPorVentaRequestDTO;
import com.clandestock.backend.venta.dto.VentaResponseDTO;
import com.clandestock.backend.venta.service.VentaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/venta")
public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService vc) {
        this.ventaService = vc;
    }

    @PostMapping("/nueva")
    public ResponseEntity<?> nueva(@RequestBody NuevaVentaRequestDTO dto) {
        try {
            VentaResponseDTO response = ventaService.nueva(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody ProductoPorVentaRequestDTO dto) {
        try {
            VentaResponseDTO response = ventaService.agregarProducto(Long.valueOf(dto.idVenta), Long.valueOf(dto.idProducto));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/quitar")
    public ResponseEntity<?> quitar(@RequestBody ProductoPorVentaRequestDTO dto) {
        try {
            VentaResponseDTO response = ventaService.quitarProducto(Long.valueOf(dto.idVenta), Long.valueOf(dto.idProducto));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            VentaResponseDTO response = ventaService.obtenerPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/insertarMetodoPago/{idMetodoPago}/{idVenta}") //insertar metodo de pago en una venta
    public ResponseEntity<?> insertarMetodoPagoEnVenta (@PathVariable Long idMetodoPago, @PathVariable Long idVenta) {
        try {
            VentaResponseDTO venta = ventaService.asignarMetodoPagoAVenta(idMetodoPago, idVenta);
            //System.out.println("Precio total sin modificar: $" + venta.precioTotal);
            //System.out.println("Precio total con metodo de pago efectuado: $" + venta.precioTotalConMetodoDePago);
            return ResponseEntity.ok(venta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/cerrar/{idVenta}")
    public ResponseEntity<?> cerarVenta (@PathVariable Long idVenta) {
        try {
            VentaResponseDTO venta = ventaService.cerrarVenta(idVenta);
            return ResponseEntity.ok(venta);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/filtrar")
    public ResponseEntity<?> filtrarVentas(@RequestBody VentaFiltroDTO filtros) {
        try {
            List<VentaResponseDTO> ventas = ventaService.filtrarVentas(filtros);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al filtrar las ventas: " + e.getMessage());
        }
    }

    @GetMapping("/abiertas")
    public ResponseEntity<?> obtenerAbiertas() {
        try {
            List<VentaResponseDTO> response = ventaService.obtenerAbiertas();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cerradas")
    public ResponseEntity<?> obtenerCerradas() {
        try {
            List<VentaResponseDTO> response = ventaService.obtenerCerradas();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener ventas cerradas");
        }
    }

    @GetMapping("/caja/{id}")
    public ResponseEntity<?> obtenerPorCaja(@PathVariable Long id) {
        try {
            List<VentaResponseDTO> response = ventaService.obtenerPorCaja(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener ventas cerradas");
        }
    }
    @PostMapping("/comanda/{id}")
    public ResponseEntity<?> imprimioComanda(@PathVariable Long id) {
        try {
            ventaService.imprimioComanda(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener ventas cerradas");
        }
    }
}
