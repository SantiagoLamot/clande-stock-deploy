package com.clandestock.backend.venta.service;

import com.clandestock.backend.venta.modelos.ProductoxVenta;
import com.clandestock.backend.venta.repository.ProductoxVentaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoxVentaService {
    private ProductoxVentaRepository productoxVentaRepository;

    public ProductoxVentaService(ProductoxVentaRepository productoxVentaRepository) {
        this.productoxVentaRepository = productoxVentaRepository;
    }

    public void guardar(ProductoxVenta prodPorVenta){
        productoxVentaRepository.save(prodPorVenta);
    }

    public ProductoxVenta obtenerPorId(Long id){
        return productoxVentaRepository.findById(id).orElseThrow(()-> new RuntimeException("Producto no encontrado en la venta"));
    }

    public void eliminar(Long id) {
    productoxVentaRepository.deleteById(id);
}
}
