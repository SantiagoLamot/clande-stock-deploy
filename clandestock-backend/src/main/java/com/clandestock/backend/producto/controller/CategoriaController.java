package com.clandestock.backend.producto.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clandestock.backend.producto.dto.CategoriaRequestDTO;
import com.clandestock.backend.producto.dto.CategoriaResponseDTO;
import com.clandestock.backend.producto.service.CategoriaService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    private CategoriaService categoriaService;

    public CategoriaController(CategoriaService cs){
        this.categoriaService = cs;
    }

    @PreAuthorize("hasAuthority('ADMIN_GENERAL')")
    @PostMapping()
    public ResponseEntity<?> guardar(@RequestBody CategoriaRequestDTO dto) {
        try{
            CategoriaResponseDTO response = categoriaService.save(dto);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    //validad que sea admin gral o el moderador adecuado
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
        try{
            CategoriaResponseDTO response = categoriaService.obtenerCategoria(Long.parseLong(id));
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    //validad que sea admin gral o el moderador adecuado
    @GetMapping("/local/{id}")
    public ResponseEntity<?> obtenerPorLocal(@PathVariable String id) {
        try{
            List<CategoriaResponseDTO> response = categoriaService.obtenerCategoriaPorLocal(Long.parseLong(id));
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @PreAuthorize("hasAuthority('ADMIN_GENERAL')")
    @PutMapping()
    public ResponseEntity<?> actualizarPorId(@RequestBody CategoriaRequestDTO dto) {
        try{
            CategoriaResponseDTO response = categoriaService.actualizar( dto);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable String id){
            try{
                categoriaService.eliminar(Long.parseLong(id));
                return ResponseEntity.ok("Categoria eliminada correctamente");
            }
            catch(RuntimeException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            catch(Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        
    }

        //obtiene todas las que correspondan segun JWT
    @GetMapping("/todas")
    public ResponseEntity<?> obtenertodas() {
        try{
            List<CategoriaResponseDTO> response = categoriaService.obtenerTodas();
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
