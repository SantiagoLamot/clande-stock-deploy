package com.clandestock.backend.venta.service;

import com.clandestock.backend.producto.modelos.ProductoPrincipal;
import com.clandestock.backend.producto.modelos.ProductoSecundario;
import com.clandestock.backend.producto.modelos.ProductoSecundarioPorPrincipal;
import com.clandestock.backend.producto.service.ProductoPrincipalService;
import com.clandestock.backend.producto.service.ProductoSecundarioPorPrincipalService;
import com.clandestock.backend.producto.service.ProductoSecundarioService;
import com.clandestock.backend.producto.service.ProductoStockService;
import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.usuario.modelos.Usuario;
import com.clandestock.backend.usuario.service.UsuarioService;
import com.clandestock.backend.venta.dto.NuevaVentaRequestDTO;
import com.clandestock.backend.venta.dto.ProductoVentaResponseDTO;
import com.clandestock.backend.venta.dto.VentaFiltroDTO;
import com.clandestock.backend.venta.dto.VentaResponseDTO;
import com.clandestock.backend.venta.modelos.*;
import com.clandestock.backend.venta.repository.MesaRepository;
import com.clandestock.backend.venta.repository.MetodoPagoRepository;
import com.clandestock.backend.venta.repository.VentaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioService usuarioService;
    private final MetodoPagoService metodoPagoService;
    private final ProductoPrincipalService productoPrincipalService;
    private final ProductoSecundarioPorPrincipalService productoSecundarioPorPrincipalService;
    private final ProductoStockService productoStockService;
    private final ProductoSecundarioService productoSecundarioService;
    private final ProductoxVentaService productoxVentaService;
    private final LocalService localService;
    private final MetodoPagoRepository metodoPagoRepository;
    private final CajaService cajaService;
    private final MesaRepository mesaRepository;

    public VentaService(
            VentaRepository ventaRepository,
            UsuarioService usuarioService,
            MetodoPagoService metodoPagoService,
            ProductoPrincipalService prodPrincipalService,
            ProductoSecundarioPorPrincipalService psxpps,
            ProductoStockService prodStockService,
            ProductoSecundarioService prodSecundarioService,
            ProductoxVentaService prodxVentaService,
            LocalService localService,
            MetodoPagoRepository metodoPagoRepository,
            CajaService cajaService,
            MesaRepository mesaRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioService = usuarioService;
        this.metodoPagoService = metodoPagoService;
        this.productoPrincipalService = prodPrincipalService;
        this.productoSecundarioPorPrincipalService = psxpps;
        this.productoStockService = prodStockService;
        this.productoSecundarioService = prodSecundarioService;
        this.productoxVentaService = prodxVentaService;
        this.localService = localService;
        this.metodoPagoRepository = metodoPagoRepository;
        this.cajaService = cajaService;
        this.mesaRepository = mesaRepository;
    }

    public VentaResponseDTO nueva(NuevaVentaRequestDTO dto) {
        UsuarioContexto usuarioContexto = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (usuarioContexto.esAdminGeneral()) {
            throw new RuntimeException("Usuario administrador no puede iniciar venta");
        }

        Usuario usuario = usuarioService.obtenerPorNombreUsuario(usuarioContexto.getNombreUsuario());
        Local local = localService.obtenerPorNombre(usuarioContexto.getLocal());
        Caja caja = cajaService.obtenerPorLocal(local, true);

        // BUSCAR MESA Y OCUPARLA, primero null por si es take away o delivery
        // Si es consumo local se asigna la mesa
        Mesa mesa = null;

        // 👉 Solo si la venta es CONSUMO_LOCAL se busca y ocupa la mesa
        if (dto.getTipoVenta() == TipoVenta.CONSUMO_LOCAL) {
            mesa = mesaRepository.findByLocal_NombreLocalAndNumeroMesa(local.getNombreLocal(), dto.getNumeroMesa())
                    .orElseThrow(() -> new RuntimeException("Mesa no encontrada en el local"));

            if (Boolean.TRUE.equals(mesa.getOcupada())) {
                throw new RuntimeException("La mesa ya está ocupada");
            }

            mesa.setOcupada(true);
            mesaRepository.save(mesa);
        }

        Venta venta = Venta.builder()
                .usuario(usuario)
                .metodoPago(null)
                .fechaApertura(LocalDateTime.now())
                .estadoPago(false)
                .precioTotal(null)
                .fechaCierre(null)
                .local(local)
                .tipoVenta(dto.tipoVenta)
                .detalleEntrega(dto.detalleEntrega)
                .caja(caja)
                .mesa(mesa != null ? mesa : null)
                .numeroMesa(mesa != null ? mesa.getNumeroMesa() : null)
                .build();
        venta = ventaRepository.save(venta);
        return toResponseDTO(venta);
    }

    public VentaResponseDTO agregarProducto(Long idVenta, Long idProducto) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        if (venta.getFechaCierre() != null || Boolean.TRUE.equals(venta.getEstadoPago())) {
            throw new RuntimeException("La venta ya está cerrada o pagada");
        }
        ProductoPrincipal producto = productoPrincipalService.obtenerPorId(idProducto);
        if (venta.getLocal() != producto.getLocal()) {
            throw new RuntimeException("Producto no corresponde al local");
        }
        List<ProductoSecundarioPorPrincipal> secundarios = productoSecundarioPorPrincipalService
                .obtenerSecundariosPorPrincipal(producto);
        // Validar stock
        int stockDisponible = secundarios.isEmpty()
                ? producto.getStock() // Si no tiene producto secundario, se guarda el stock del principal
                : productoStockService.calcularStockDisponible(secundarios);
        if (stockDisponible < 1) {
            throw new RuntimeException("Stock insuficiente para agregar este producto");
        }
        // Descontar stock
        if (secundarios.isEmpty()) {
            producto.setStock(producto.getStock() - 1);
            productoPrincipalService.actualizarStock(producto);
        } else {
            Map<Long, Long> requerimientos = secundarios.stream()
                    .collect(Collectors.groupingBy(
                            rel -> rel.getProductoSecundario().getId(),
                            Collectors.counting()));
            for (Map.Entry<Long, Long> entry : requerimientos.entrySet()) {
                ProductoSecundario sec = productoSecundarioService.obtenerPorId(entry.getKey());
                sec.setStock(sec.getStock() - entry.getValue().intValue());
                productoSecundarioService.actualizarStock(sec);
            }
        }
        // Crear ProductoPorVenta
        ProductoxVenta pxv = ProductoxVenta.builder()
                .venta(venta)
                .idProducto(producto.getId())
                .nombreProducto(producto.getNombreProducto())
                .precioProducto(producto.getPrecioProducto())
                .comanda(producto.getComanda())
                .build();
        productoxVentaService.guardar(pxv);
        // Actualizar venta
        venta.getProductos().add(pxv);

        BigDecimal nuevoTotal = venta.getPrecioTotal() == null
                ? producto.getPrecioProducto()
                : venta.getPrecioTotal().add(producto.getPrecioProducto());
        venta.setPrecioTotal(nuevoTotal);
        ventaRepository.save(venta);

        // Actualizar monto en caso de tener metodo de pago insertado
        if (venta.getMetodoPago() != null) {
            BigDecimal precioFinal = calcularPrecioFinal(venta.getPrecioTotal(), venta.getMetodoPago());

            venta.setPrecioTotalConMetodoDePago(precioFinal);

            Venta ventaConMetodoDePagoIncluido = ventaRepository.save(venta);
        }

        return toResponseDTO(venta);
    }

    public VentaResponseDTO quitarProducto(Long idVenta, Long idProductoxVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        if (venta.getFechaCierre() != null || Boolean.TRUE.equals(venta.getEstadoPago())) {
            throw new RuntimeException("La venta ya está cerrada o pagada");
        }
        ProductoxVenta pxv = productoxVentaService.obtenerPorId(idProductoxVenta);
        if (venta != pxv.getVenta()) {
            throw new RuntimeException("Producto no corresponde a la venta seleccionada");
        }
        ProductoPrincipal producto = productoPrincipalService.obtenerPorId(pxv.getIdProducto());
        List<ProductoSecundarioPorPrincipal> secundarios = productoSecundarioPorPrincipalService
                .obtenerSecundariosPorPrincipal(producto);
        // Revertir stock
        if (secundarios.isEmpty()) {
            producto.setStock(producto.getStock() + 1);
            productoPrincipalService.actualizarStock(producto);
        } else {
            Map<Long, Long> requerimientos = secundarios.stream()
                    .collect(Collectors.groupingBy(
                            rel -> rel.getProductoSecundario().getId(),
                            Collectors.counting()));
            for (Map.Entry<Long, Long> entry : requerimientos.entrySet()) {
                ProductoSecundario sec = productoSecundarioService.obtenerPorId(entry.getKey());
                sec.setStock(sec.getStock() + entry.getValue().intValue());
                productoSecundarioService.actualizarStock(sec);
            }
        }
        // Eliminar entrada
        productoxVentaService.eliminar(pxv.getId());
        // Actualizar venta
        venta.getProductos().removeIf(p -> p.getId().equals(idProductoxVenta));

        BigDecimal nuevoTotal = venta.getPrecioTotal().subtract(pxv.getPrecioProducto());
        venta.setPrecioTotal(nuevoTotal.compareTo(BigDecimal.ZERO) > 0 ? nuevoTotal : null);
        ventaRepository.save(venta);

        // Actualizar monto en caso de tener metodo de pago insertado
        if (venta.getMetodoPago() != null) {
            BigDecimal precioFinal = calcularPrecioFinal(venta.getPrecioTotal(), venta.getMetodoPago());

            venta.setPrecioTotalConMetodoDePago(precioFinal);

            Venta ventaConMetodoDePagoIncluido = ventaRepository.save(venta);
        }

        return toResponseDTO(venta);
    }

    public VentaResponseDTO obtenerPorId(Long id) {
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        return toResponseDTO(venta);
    }

    private VentaResponseDTO toResponseDTO(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.idVenta = venta.getId().toString();
        dto.idUsuario = venta.getUsuario().getId().toString();
        dto.idMetodoPago = venta.getMetodoPago() != null ? venta.getMetodoPago().getId().toString() : null;
        dto.precioTotal = venta.getPrecioTotal() != null ? venta.getPrecioTotal().toString() : null;
        dto.precioTotalConMetodoDePago = venta.getPrecioTotalConMetodoDePago() != null
                ? venta.getPrecioTotalConMetodoDePago().toString()
                : null;
        dto.fechaApertura = venta.getFechaApertura().toString();
        dto.fechaCierre = venta.getFechaCierre() != null ? venta.getFechaCierre().toString() : null;
        dto.estadoPago = venta.getEstadoPago().toString();
        dto.tipoVenta = venta.getTipoVenta().toString();
        dto.detalleEntrega = venta.getDetalleEntrega();
        dto.localId = venta.getLocal().getId().toString();
        dto.numMesa = venta.getNumeroMesa() != null ? venta.getNumeroMesa().toString() : null;
        dto.productos = venta.getProductos() != null ? venta.getProductos().stream().map(p -> {
            ProductoVentaResponseDTO prod = new ProductoVentaResponseDTO();
            prod.idProductoPorVenta = p.getId().toString();
            prod.idProducto = p.getIdProducto().toString();
            prod.nombreProducto = p.getNombreProducto();
            prod.precioProducto = p.getPrecioProducto().toString();
            prod.comanda = p.getComanda().toString();
            return prod;
        }).toList() : List.of();
        return dto;
    }

    // CALCULO DE PRECIO POR METODO DE PAGO.

    @Transactional
    public VentaResponseDTO asignarMetodoPagoAVenta(Long idMetodoPago, Long idVenta) {
        MetodoPago metodoPago = metodoPagoRepository.findById(idMetodoPago)
                .orElseThrow(
                        () -> new EntityNotFoundException("Método de pago con ID " + idMetodoPago + " no existente"));

        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException("Venta con ID " + idVenta + " no existente"));

        BigDecimal precioOriginal = venta.getPrecioTotal();
        if (precioOriginal == null) {
            throw new RuntimeException("La venta aún no tiene un precio total definido");
        }

        BigDecimal precioFinal = calcularPrecioFinal(precioOriginal, metodoPago);

        venta.setMetodoPago(metodoPago);
        venta.setPrecioTotalConMetodoDePago(precioFinal);

        Venta ventaConMetodoDePagoIncluido = ventaRepository.save(venta);

        return toResponseDTO(ventaConMetodoDePagoIncluido);
    }

    private BigDecimal calcularPrecioFinal(BigDecimal precioOriginal, MetodoPago metodoPago) {
        BigDecimal precioFinal = precioOriginal;

        if (metodoPago.getDescuento() != 0) {
            BigDecimal descuento = BigDecimal.valueOf(metodoPago.getDescuento())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            precioFinal = precioOriginal.subtract(precioOriginal.multiply(descuento));

            // System.out.println("Entro a descuento");
        } else if (metodoPago.getIncremento() != 0) {
            BigDecimal incremento = BigDecimal.valueOf(metodoPago.getIncremento())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            precioFinal = precioOriginal.add(precioOriginal.multiply(incremento));
            // System.out.println("Entro a incremento");
        }

        return precioFinal.setScale(2, RoundingMode.HALF_UP);
    }

    // CERRAR VENTA

    public VentaResponseDTO cerrarVenta(Long idVenta) {
        Venta ventaPorCerrar = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new EntityNotFoundException("Venta con ID " + idVenta + " no existente "));

        if (ventaPorCerrar.getFechaCierre() != null && Boolean.TRUE.equals(ventaPorCerrar.getEstadoPago())) {
            throw new IllegalStateException("La venta ya está cerrada");
        }

        if (ventaPorCerrar.getMetodoPago() == null && ventaPorCerrar.getPrecioTotalConMetodoDePago() == null) {
            throw new RuntimeException("La venta que desea cerrar aun no tiene asignado un metodo de pago");
        }

        ventaPorCerrar.setFechaCierre(LocalDateTime.now());
        ventaPorCerrar.setEstadoPago(true);

        // Si es consumo local, liberar la mesa
        if (ventaPorCerrar.getTipoVenta() == TipoVenta.CONSUMO_LOCAL && ventaPorCerrar.getMesa() != null) {
            Mesa mesa = ventaPorCerrar.getMesa();
            mesa.setOcupada(false);
            mesaRepository.save(mesa);
        }

        Venta ventaCerrada = ventaRepository.save(ventaPorCerrar);

        return toResponseDTO(ventaCerrada);
    }

    public List<VentaResponseDTO> filtrarVentas(VentaFiltroDTO filtros) {
        Specification<Venta> spec = Specification.where(null);

        if (filtros.usuarioId() != null) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("usuario").get("id"), Long.parseLong(filtros.usuarioId())));
        }
        if (filtros.metodoPagoId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("metodoPago").get("id"),
                    Long.parseLong(filtros.metodoPagoId())));
        }
        if (filtros.localId() != null) {
            spec = spec
                    .and((root, query, cb) -> cb.equal(root.get("local").get("id"), Long.parseLong(filtros.localId())));
        }
        if (filtros.cajaId() != null) {
            spec = spec
                    .and((root, query, cb) -> cb.equal(root.get("caja").get("id"), Long.parseLong(filtros.cajaId())));
        }
        if (filtros.fechaDesde() != null && filtros.fechaHasta() != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("fechaApertura"), filtros.fechaDesde(),
                    filtros.fechaHasta()));
        }

        List<Venta> ventas = ventaRepository.findAll();

        return ventas.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<VentaResponseDTO> obtenerAbiertas() {
        // Se obtiene el contexto seteado cuando pasa el jwt authentication filters
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        // Se verifica si es admin general y obtiene todas
        if (usuario.esAdminGeneral()) {
            return ventaRepository.findByEstadoPagoIsFalse()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());

            // Caso contario obtiene la corresponiende al local asignado
        } else {

            return ventaRepository.findByLocal_NombreLocalAndEstadoPagoIsFalse(usuario.getLocal())
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        }

    }

    public List<VentaResponseDTO> obtenerCerradas() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (usuario.esAdminGeneral()) {
            return ventaRepository.findByEstadoPagoIsTrue()
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        } else {
            Local local = localService.obtenerPorNombre(usuario.getLocal());
            Caja cajaAbierta = cajaService.obtenerPorLocal(local, true);

            if (cajaAbierta == null) {
                throw new RuntimeException("No hay caja abierta actualmente en el local");
            }

            return ventaRepository.findByCajaAndEstadoPagoIsTrue(cajaAbierta)
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<VentaResponseDTO> obtenerPorCaja(Long idCaja) {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (!usuario.esAdminGeneral()) {
            throw new RuntimeException("Funcion solo de administrador");
        } else {

            return ventaRepository.findByCaja_Id(idCaja)
                    .stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());

        }
    }

}
