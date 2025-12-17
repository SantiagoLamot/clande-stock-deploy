package com.clandestock.backend.venta.service;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.ResumenProductoVendidoResponseDTO;
import com.clandestock.backend.venta.modelos.Caja;
import com.clandestock.backend.venta.modelos.ProductoxVenta;
import com.clandestock.backend.venta.repository.CajaRepository;
import com.clandestock.backend.venta.repository.ProductoxVentaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductoxVentaService {
    private ProductoxVentaRepository productoxVentaRepository;
    private CajaRepository cajaRepository;

    public ProductoxVentaService(ProductoxVentaRepository productoxVentaRepository, CajaRepository cajaRepository) {
        this.productoxVentaRepository = productoxVentaRepository;
        this.cajaRepository = cajaRepository;
    }

    public void guardar(ProductoxVenta prodPorVenta) {
        productoxVentaRepository.save(prodPorVenta);
    }

    public ProductoxVenta obtenerPorId(Long id) {
        return productoxVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en la venta"));
    }

    public void eliminar(Long id) {
        productoxVentaRepository.deleteById(id);
    }

    public List<ResumenProductoVendidoResponseDTO> obtenerResumenProductos(Long cajaId) {

        List<ProductoxVenta> productos = productoxVentaRepository.findByCajaId(cajaId);

        // Agrupar por nombreProducto y contar
        Map<String, Long> agrupado = productos.stream()
                .collect(Collectors.groupingBy(
                        ProductoxVenta::getNombreProducto,
                        Collectors.counting()
                ));

        // Convertir a DTO
        return agrupado.entrySet().stream()
                .map(e -> {
                    ResumenProductoVendidoResponseDTO dto = new ResumenProductoVendidoResponseDTO();
                    dto.cantidad = String.valueOf(e.getValue());
                    dto.nombreProducto = e.getKey();
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ResumenProductoVendidoResponseDTO> obtenerResumenCajaAbierta() {
        
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();


        Caja cajaAbierta;

        if (usuario.esAdminGeneral()) {
            throw new RuntimeException("Funcion de moderador");
        } else {
            cajaAbierta = cajaRepository.findByLocal_NombreLocalAndEstado(usuario.getLocal(), true)
                    .stream()
                    .findFirst()
                    .orElse(null);
        }

        if (cajaAbierta == null) {
            return List.of();
        }

        return obtenerResumenProductos(cajaAbierta.getId());
    }
}
