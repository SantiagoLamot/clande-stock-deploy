package com.clandestock.backend.producto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.clandestock.backend.producto.repository.ProductoPrincipalRepository;
import org.springframework.stereotype.Service;

import com.clandestock.backend.producto.dto.ProductoStockResponseDTO;
import com.clandestock.backend.producto.modelos.ProductoPrincipal;
import com.clandestock.backend.producto.modelos.ProductoSecundario;
import com.clandestock.backend.producto.modelos.ProductoSecundarioPorPrincipal;

@Service
public class ProductoStockService {

    private ProductoPrincipalService productoPrincipalService;
    private ProductoSecundarioPorPrincipalService productoSecundarioPorPrincipalService;
    private ProductoSecundarioService productoSecundarioService;

    public ProductoStockService(ProductoPrincipalService pps, ProductoSecundarioPorPrincipalService psxpps,
            ProductoSecundarioService productoSecundarioService,
            ProductoPrincipalRepository productoPrincipalRepository) {
        this.productoPrincipalService = pps;
        this.productoSecundarioPorPrincipalService = psxpps;
        this.productoSecundarioService = productoSecundarioService;
    }

    // Tiene filtro de JWT aplicado(retorna segun el rol del token)
    public List<ProductoStockResponseDTO> productosConStockSinFiltro() {
        List<ProductoPrincipal> productos = productoPrincipalService.obtenerTodosEntity();
        List<ProductoStockResponseDTO> resultado = new ArrayList<>();

        for (ProductoPrincipal principal : productos) {
            List<ProductoSecundarioPorPrincipal> secundarios = productoSecundarioPorPrincipalService
                    .obtenerSecundariosPorPrincipal(principal);

            int stockDisponible;
            if (secundarios.isEmpty()) {
                stockDisponible = principal.getStock();
            } else {
                stockDisponible = calcularStockDisponible(secundarios);
            }

            resultado.add(new ProductoStockResponseDTO(
                    principal.getId().toString(),
                    principal.getNombreProducto(),
                    String.valueOf(stockDisponible),
                    principal.getPrecioProducto().toString(),
                    principal.getAletarStock()>=stockDisponible ?"true":"false",
                    principal.getStockBajo().toString(),
                    principal.getSinStock().toString(),
                    principal.getLocal().getId().toString(),
                    secundarios.isEmpty() ? "false" : "true",
                    principal.getComanda().toString()));
        }

        return resultado;
    }

    public List<ProductoStockResponseDTO> productosConStock(Long categoriaID) {
        // Obtener los productos del local según el usuario autenticado
        List<ProductoPrincipal> productos;

        if (categoriaID != null) {
            productos = productoPrincipalService.obtenerPorCategoria(categoriaID);
        } else {
            productos = productoPrincipalService.obtenerTodosEntity();
        }

        List<ProductoStockResponseDTO> resultado = new ArrayList<>();

        for (ProductoPrincipal principal : productos) {
            List<ProductoSecundarioPorPrincipal> secundarios = productoSecundarioPorPrincipalService
                    .obtenerSecundariosPorPrincipal(principal);

            int stockDisponible = secundarios.isEmpty()
                    ? principal.getStock()
                    : calcularStockDisponible(secundarios);

            resultado.add(new ProductoStockResponseDTO(
                    principal.getId().toString(),
                    principal.getNombreProducto(),
                    String.valueOf(stockDisponible),
                    principal.getPrecioProducto().toString(),
                    principal.getAletarStock()>=stockDisponible ?"true":"false",
                    principal.getStockBajo().toString(),
                    principal.getSinStock().toString(),
                    principal.getLocal().getId().toString(),
                    secundarios.isEmpty() ? "false" : "true",
                    principal.getComanda().toString()));
        }

        return resultado;
    }

    public int calcularStockDisponible(List<ProductoSecundarioPorPrincipal> relaciones) {
        // Agrupar por producto secundario y contar cuántas veces aparece
        Map<Long, Long> requerimientos = relaciones.stream()
                .collect(Collectors.groupingBy(
                        rel -> rel.getProductoSecundario().getId(),
                        Collectors.counting()));

        // Mapear ID a entidad para acceder al stock
        Map<Long, ProductoSecundario> secundariosMap = relaciones.stream()
                .map(ProductoSecundarioPorPrincipal::getProductoSecundario)
                .collect(Collectors.toMap(
                        ProductoSecundario::getId,
                        Function.identity(),
                        (a, b) -> a // en caso de duplicado, conservar uno
                ));

        // Calcular el mínimo stock disponible según los requerimientos
        return requerimientos.entrySet().stream()
                .mapToInt(entry -> {
                    ProductoSecundario sec = secundariosMap.get(entry.getKey());
                    int stock = Math.max(0, sec.getStock());
                    long vecesRequerido = entry.getValue();
                    return (int) (stock / vecesRequerido);
                })
                .min()
                .orElse(0);
    }

    public List<ProductoStockResponseDTO> productosPrimariosEnAlerta() {
        List<ProductoPrincipal> productos = productoPrincipalService.obtenerTodosEntity();
        List<ProductoStockResponseDTO> resultado = new ArrayList<>();

        for (ProductoPrincipal principal : productos) {
            List<ProductoSecundarioPorPrincipal> secundarios = productoSecundarioPorPrincipalService
                    .obtenerSecundariosPorPrincipal(principal);

            int stockDisponible = secundarios.isEmpty()
                    ? principal.getStock()
                    : calcularStockDisponible(secundarios);

            boolean alertaPorBooleanos = Boolean.TRUE.equals(principal.getSinStock())
                    || Boolean.TRUE.equals(principal.getStockBajo());
            boolean alertaPorCantidad = principal.getAletarStock() >= stockDisponible
                    || stockDisponible <= principal.getAletarStock();

            if (alertaPorBooleanos) {
                resultado.add(new ProductoStockResponseDTO(
                        principal.getId().toString(),
                        principal.getNombreProducto(),
                        String.valueOf(stockDisponible),
                        principal.getPrecioProducto().toString(),
                        "false",
                        principal.getStockBajo().toString(),
                        principal.getSinStock().toString(),
                        principal.getLocal().getId().toString(),
                        secundarios.isEmpty() ? "false" : "true",
                        principal.getComanda().toString()));
            }
            else if (alertaPorCantidad) {
                resultado.add(new ProductoStockResponseDTO(
                        principal.getId().toString(),
                        principal.getNombreProducto(),
                        String.valueOf(stockDisponible),
                        principal.getPrecioProducto().toString(),
                        "true",
                        "false",
                        "false",
                        principal.getLocal().getId().toString(),
                        secundarios.isEmpty() ? "false" : "true",
                        principal.getComanda().toString()));
            }
        }

        return resultado;
    }

    public List<ProductoStockResponseDTO> productosSecundariosEnAlerta() {
        List<ProductoSecundario> secundarios = productoSecundarioService.obtenerTodos();
        List<ProductoStockResponseDTO> resultado = new ArrayList<>();

        for (ProductoSecundario sec : secundarios) {
            boolean alertaPorBooleanos = Boolean.TRUE.equals(sec.getSinStock())
                    || Boolean.TRUE.equals(sec.getStockBajo());
            boolean alertaPorCantidad = sec.getAletarStock() > 0 && sec.getStock() <= sec.getAletarStock();

            if (alertaPorBooleanos) {
                resultado.add(new ProductoStockResponseDTO(
                        sec.getId().toString(),
                        sec.getNombreProducto(),
                        String.valueOf(sec.getStock()),
                        "0.00",
                        "false",
                        sec.getStockBajo().toString(),
                        sec.getSinStock().toString(),
                        sec.getLocal().getId().toString(),
                        "false",
                        "false"));
            }
            else if (alertaPorCantidad) {
                resultado.add(new ProductoStockResponseDTO(
                        sec.getId().toString(),
                        sec.getNombreProducto(),
                        String.valueOf(sec.getStock()),
                        "0.00",
                        "true",
                        "false",
                        "false",
                        sec.getLocal().getId().toString(),
                        "false",
                        "false"));
            }
        }
        return resultado;
    }
}