package com.clandestock.backend.venta.controller;


import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.MetodoPagoRequestDTO;
import com.clandestock.backend.venta.dto.MetodoPagoResponseDTO;
import com.clandestock.backend.venta.service.MetodoPagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/metodopago")
public class MetodoPagoController {

    private MetodoPagoService metodoPagoService;

    public MetodoPagoController (MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    //Recibe id de local, y no obtiene de usuario porque el unico que va a agregar metodos de pago es el administrador
    //Es decir, va a tener que elegir a que local, por lo tanto se enviara ID de local.
    @PostMapping
    public ResponseEntity<?> insertarMetodoPago (@RequestBody MetodoPagoRequestDTO dto) {
        try{
            MetodoPagoResponseDTO response = metodoPagoService.insertarMetodoPago(dto);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping
    public ResponseEntity<?> actualizarMetodoPago (@RequestBody MetodoPagoRequestDTO dto) {
        try {
            MetodoPagoResponseDTO response = metodoPagoService.actualizarMetodoPago(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/baja/{id}")
    public ResponseEntity<?> bajaLogicaMetodoPago (@PathVariable Long id) {
        try {
            MetodoPagoResponseDTO response = metodoPagoService.bajaLogica(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/alta/{id}")
    public ResponseEntity<?> altaLogicaMetodoPago (@PathVariable Long id) {
        try {
            MetodoPagoResponseDTO response = metodoPagoService.altaLogica(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerMetodosPago() {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<MetodoPagoResponseDTO> lista = metodoPagoService.listarMetodosPagoPorUsuario(usuario);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener métodos de pago: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener métodos de pago");
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<?> obtenerMetodosPagoActivos() {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            List<MetodoPagoResponseDTO> lista = metodoPagoService.listarMetodosActivosPorUsuario(usuario);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener métodos de pago activos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener métodos de pago activos");
        }
    }

    @GetMapping("/inactivos")
    public ResponseEntity<?> obtenerMetodosPagoInactivos() {
        try {
            UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            List<MetodoPagoResponseDTO> lista = metodoPagoService.listarMetodosInactivosPorUsuario(usuario);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener métodos de pago inactivos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al obtener métodos de pago inactivos");
        }
    }




    //extras
    @GetMapping("/local/{id}")
    public ResponseEntity<?> obtenerMetodosPagoPorLocal(@PathVariable("id") Long localId) {
        try {
            List<MetodoPagoResponseDTO> lista = metodoPagoService.listarPorLocalId(localId);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }
    @GetMapping("/local/{id}/activos")
    public ResponseEntity<?> obtenerMetodosPagoActivosPorLocal(@PathVariable("id") Long localId) {
        try {
            List<MetodoPagoResponseDTO> lista = metodoPagoService.listarActivosPorLocal(localId);
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

}
