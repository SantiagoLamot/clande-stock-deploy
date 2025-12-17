package com.clandestock.backend.producto.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.clandestock.backend.producto.dto.CategoriaRequestDTO;
import com.clandestock.backend.producto.dto.CategoriaResponseDTO;
import com.clandestock.backend.producto.modelos.Categoria;
import com.clandestock.backend.producto.repository.CategoriaRepository;
import com.clandestock.backend.producto.repository.ProductoPrincipalRepository;
import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.service.LocalService;

@Service
public class CategoriaService {
    private CategoriaRepository categoriaRepository;
    private LocalService localService;
    private ProductoPrincipalRepository productoPpalRepository;

    public CategoriaService(CategoriaRepository cr, LocalService ls, ProductoPrincipalRepository ppr) {
        this.categoriaRepository = cr;
        this.localService = ls;
        this.productoPpalRepository = ppr;
    }

    public CategoriaResponseDTO save(CategoriaRequestDTO dto) {
        Categoria nueva = categoriaRepository.save(toEntity(dto));
        return toResponseDTO(nueva);
    }

    public CategoriaResponseDTO obtenerCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> obtenerCategoriaPorLocal(Long id) {
        List<Categoria> categorias = categoriaRepository.findByLocalId(id);
        if (categorias.isEmpty()) {
            throw new RuntimeException("No se encontraron categorias");
        }
        return categorias.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO actualizar(CategoriaRequestDTO dto) {

        Categoria categoria = categoriaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        Local local = localService.obtenerPorId(Long.parseLong(dto.getLocalId()));
        categoria.setLocal(local);
        categoria.setNombreCategoria(dto.getNombreCategoria());
        Categoria catActualizada = categoriaRepository.save(categoria);
        return toResponseDTO(catActualizada);
    }

    public void eliminar(Long id) {
        if (productoPpalRepository.countByCategoriaId(id) == 0L) {
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            categoriaRepository.delete(categoria);
        } else {
            new RuntimeException("No se puede eliminar categoria en uso, quite todos los productos");
        }
    }

    public Categoria obtenerCategoriaEntity(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return categoria;
    }

    //Validado por el context
    public List<CategoriaResponseDTO> obtenerTodas() {
        //Se obtiene el contexto seteado cuando pasa el jwt authentication filters
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        //Se verifica si es admin general y obtiene todas
        if (usuario.esAdminGeneral()) {
            return categoriaRepository.findAll()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());

            //Caso contario obtiene la corresponiende al local asignado
        } else {
            return categoriaRepository.findByLocal_NombreLocal(usuario.getLocal())
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    // =======================CASTEO DE ENTIDAD<--->DTO==========================
    private Categoria toEntity(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        if (dto.getId() != null) {
            Categoria cat = categoriaRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            categoria.setId(cat.getId());
        }
        categoria.setNombreCategoria(dto.getNombreCategoria());
        categoria.setLocal(localService.obtenerPorId(Long.parseLong(dto.getLocalId())));
        return categoria;
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombreCategoria(categoria.getNombreCategoria());
        dto.setLocalId(categoria.getLocal().getId());
        dto.setActivo(categoria.isActivo());
        return dto;
    }

    public CategoriaResponseDTO darDeBaja(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        categoria.setActivo(false);
        categoriaRepository.save(categoria);

        return toResponseDTO(categoria);
    }

    public CategoriaResponseDTO reactivarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        categoria.setActivo(true);
        categoriaRepository.save(categoria);

        return toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> obtenerCategoriasActivas() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (usuario.esAdminGeneral()) {
            return categoriaRepository.findByActivoTrue()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        } else {
            return categoriaRepository.findByLocal_NombreLocalAndActivoTrue(usuario.getLocal())
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<CategoriaResponseDTO> obtenerInactivas() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!usuario.esAdminGeneral()) {
            throw new AccessDeniedException("No tienes permisos para ver categorías inactivas");
        }
        List<Categoria> categorias = categoriaRepository.findByActivoFalse();
        if (categorias.isEmpty()) {
            throw new RuntimeException("No se encontraron categorías inactivas");
        }
        return categorias.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
