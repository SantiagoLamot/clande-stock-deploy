package com.clandestock.backend.producto.service;

import com.clandestock.backend.producto.dto.ProductoSecundarioPorPrincipalRequestDTO;
import com.clandestock.backend.producto.dto.ProductoSecundarioPorPrincipalResponseDTO;
import com.clandestock.backend.producto.dto.ProductoSecundarioResponseDTO;
import com.clandestock.backend.producto.modelos.ProductoPrincipal;
import com.clandestock.backend.producto.modelos.ProductoSecundario;
import com.clandestock.backend.producto.modelos.ProductoSecundarioPorPrincipal;
import com.clandestock.backend.producto.repository.ProductoPrincipalRepository;
import com.clandestock.backend.producto.repository.ProductoSecundarioRepository;
import org.springframework.stereotype.Service;

import com.clandestock.backend.producto.repository.ProductoSecundarioPorPrincipalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoSecundarioPorPrincipalService {
    private ProductoSecundarioPorPrincipalRepository relacionRepository;
    private ProductoPrincipalService productoPrincipalService;
    private ProductoSecundarioService productoSecundarioService;

    public ProductoSecundarioPorPrincipalService(ProductoSecundarioPorPrincipalRepository relacionRepository,
            ProductoSecundarioRepository productoSecundarioRepository,
            ProductoPrincipalRepository productoPrincipalRepository,
            ProductoPrincipalService productoPrincipalService,
            ProductoSecundarioService productoSecundarioService) {
        this.relacionRepository = relacionRepository;
        this.productoPrincipalService = productoPrincipalService;
        this.productoSecundarioService = productoSecundarioService;
    }

    // CRUD
    public ProductoSecundarioPorPrincipalResponseDTO guardarRelacion(ProductoSecundarioPorPrincipalRequestDTO dto) {
        // if (relacionRepository.existsByProductoPrimarioAndProductoSecundario())

        ProductoSecundarioPorPrincipal entidad = toEntity(dto);
        ProductoSecundarioPorPrincipal guardado = relacionRepository.save(entidad);
        return toResponseDTO(guardado);
    }

    public List<ProductoSecundarioResponseDTO> obtenerSecundariosPorPrincipal(Long idPrincipal) {
        List<ProductoSecundarioPorPrincipal> relaciones = relacionRepository.findByProductoPrimario_Id(idPrincipal);

        return relaciones.stream()
                .map(relacion -> toProductoSecundarioDTO(relacion.getProductoSecundario(), relacion.getId()))
                .collect(Collectors.toList());
    }

    public List<ProductoSecundarioPorPrincipal> obtenerSecundariosPorPrincipal(ProductoPrincipal pPrincipal) {
        List<ProductoSecundarioPorPrincipal> relaciones = relacionRepository
                .findByProductoPrimario_Id(pPrincipal.getId());

        return relaciones;
    }

    public void eliminarRelacion(Long idRelacion) {
        if (!relacionRepository.existsById(idRelacion)) {
            throw new RuntimeException("La relacion con ID " + idRelacion + " no existe");
        }

        relacionRepository.deleteById(idRelacion);
    }

    // CASTEOS
    private ProductoSecundarioResponseDTO toProductoSecundarioDTO(ProductoSecundario producto, Long idRelacion) {
        return new ProductoSecundarioResponseDTO(
                producto.getId().toString(),
                producto.getNombreProducto(),
                String.valueOf(producto.getStock()),
                producto.getEstado().toString(),
                producto.getLocal().getId().toString(),
                Integer.toString(producto.getAletarStock()),
                producto.getStockBajo().toString(),
                producto.getSinStock().toString(),
                idRelacion.toString());
    }

    private ProductoSecundarioPorPrincipalResponseDTO toResponseDTO(ProductoSecundarioPorPrincipal entidad) {
        return new ProductoSecundarioPorPrincipalResponseDTO(
                entidad.getId().toString(),
                entidad.getProductoPrimario().getId().toString(),
                entidad.getProductoSecundario().getId().toString());
    }

    private ProductoSecundarioPorPrincipal toEntitySinID(ProductoSecundarioPorPrincipalRequestDTO dto) {
        ProductoPrincipal productoPrincipal = productoPrincipalService.obtenerPorId(
                Long.parseLong(dto.id_producto_principal()));

        ProductoSecundario productoSecundario = productoSecundarioService.obtenerPorId(
                Long.parseLong(dto.id_producto_secundario()));

        return ProductoSecundarioPorPrincipal.builder()
                .productoPrimario(productoPrincipal)
                .productoSecundario(productoSecundario)
                .build();
    }

    private ProductoSecundarioPorPrincipal toEntity(ProductoSecundarioPorPrincipalRequestDTO dto) {
        ProductoPrincipal productoPrincipal = productoPrincipalService.obtenerPorId(
                Long.parseLong(dto.id_producto_principal()));

        ProductoSecundario productoSecundario = productoSecundarioService.obtenerPorId(
                Long.parseLong(dto.id_producto_secundario()));

        ProductoSecundarioPorPrincipal.ProductoSecundarioPorPrincipalBuilder builder = ProductoSecundarioPorPrincipal
                .builder()
                .productoPrimario(productoPrincipal)
                .productoSecundario(productoSecundario);

        if (dto.id() != null && !dto.id().isBlank()) {
            builder.id(Long.parseLong(dto.id()));
        }

        return builder.build();
    }

}
