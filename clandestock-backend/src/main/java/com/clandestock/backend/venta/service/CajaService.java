package com.clandestock.backend.venta.service;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.usuario.modelos.Usuario;
import com.clandestock.backend.usuario.service.UsuarioService;
import com.clandestock.backend.venta.dto.DetalleCajaResponseDTO;
import com.clandestock.backend.venta.dto.ReporteCajaResponseDTO;
import com.clandestock.backend.venta.dto.CajaResponseDTO;
import com.clandestock.backend.venta.modelos.Caja;
import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.repository.CajaRepository;
import com.clandestock.backend.venta.repository.VentaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CajaService {
    private CajaRepository cajaRepository;
    private UsuarioService usuarioService;
    private LocalService localService;
    private final VentaRepository ventaRepository;

    public CajaService(CajaRepository cajaRepository, UsuarioService usuarioService, LocalService localService,
            VentaRepository ventaRepository) {
        this.cajaRepository = cajaRepository;
        this.usuarioService = usuarioService;
        this.localService = localService;
        this.ventaRepository = ventaRepository;
    }

    public CajaResponseDTO abrir(BigDecimal montoApertura) {
        // Se obtiene el contexto seteado cuando pasa el jwt authentication filters
        UsuarioContexto usuarioContext = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Usuario usuario = usuarioService.obtenerPorNombreUsuario(usuarioContext.getNombreUsuario());
        Local local = localService.obtenerPorNombre(usuarioContext.getLocal());

        // Verifica si ya existe una caja abierta en ese local
        Optional<Caja> cajaAbierta = cajaRepository.findByLocalAndEstado(local, true);
        if (cajaAbierta.isPresent()) {
            throw new RuntimeException("Ya existe una caja abierta en este local");
        }

        Caja caja = new Caja();
        caja.setUsuario(usuario);
        caja.setFechaApertura(LocalDateTime.now());
        caja.setMontoApertura(montoApertura);
        caja.setLocal(local);

        Caja nuevCaja = cajaRepository.save(caja);
        return toDTO(nuevCaja);
    }

    public Caja obtenerPorLocal(Local local, Boolean cajaAbierta) {
        return cajaRepository.findByLocalAndEstado(local, cajaAbierta)
                .orElseThrow(() -> new RuntimeException("El local no posee una caja abierta"));
    }

    public CajaResponseDTO cerrar() {
        UsuarioContexto usuarioContext = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Local local = localService.obtenerPorNombre(usuarioContext.getLocal());
        Caja caja = cajaRepository.findByLocalAndEstado(local, true)
                .orElseThrow(() -> new RuntimeException("No hay caja abierta en este local"));

        // Verifico que no haya ventas abiertas en esa caja
        boolean hayVentasAbiertas = cajaPoseeVentasAbiertas(caja);
        if (hayVentasAbiertas) {
            throw new RuntimeException("No se puede cerrar la caja: existen ventas abiertas");
        }

        caja.setEstado(false);
        caja.setFechaCierre(LocalDateTime.now());
        Caja cerrada = cajaRepository.save(caja);
        return toDTO(cerrada);
    }

    private boolean cajaPoseeVentasAbiertas(Caja caja) {
        return ventaRepository.existsByCajaAndEstadoPago(caja, false)
                || ventaRepository.existsByCajaAndFechaCierreIsNull(caja);
    }

    public List<ReporteCajaResponseDTO> listarCajasCerradas() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (!usuario.esAdminGeneral()) {
            throw new RuntimeException("Se necesita permiso de administrador");
        }

        List<Object[]> resultados = ventaRepository.obtenerTotalesPorCajaYMetodo();
        Map<Long, ReporteCajaResponseDTO> reporteMap = new HashMap<>();

        for (Object[] fila : resultados) {
            Long cajaId = (Long) fila[0];
            String metodoPago = (String) fila[1];
            BigDecimal total = (BigDecimal) fila[2];
            LocalDateTime fechaApertura = (LocalDateTime) fila[3];
            LocalDateTime fechaCierre = (LocalDateTime) fila[4];
            Long idLocal = (Long) fila[5];

            ReporteCajaResponseDTO reporte = reporteMap.getOrDefault(
                    cajaId,
                    new ReporteCajaResponseDTO(cajaId, idLocal.toString(), BigDecimal.ZERO, new ArrayList<>(),
                            fechaApertura,
                            fechaCierre));

            reporte.getDetallePorMetodo().add(new DetalleCajaResponseDTO(cajaId, metodoPago, total));
            reporte.setTotalGeneral(reporte.getTotalGeneral().add(total));
            reporte.setFechaApertura(fechaApertura);
            reporte.setFechaCierre(fechaCierre);
            reporte.setIdLocal(idLocal.toString());

            reporteMap.put(cajaId, reporte);
        }

        return new ArrayList<>(reporteMap.values());
    }

    public List<ReporteCajaResponseDTO> listarCajasAbiertas() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Object[]> resultados;

        if (usuario.esAdminGeneral()) {
            // Admin general ve todas las cajas abiertas
            return listarCajasAbiertasAdmin();
        } else {
            // Moderador: obtiene solo la caja abierta de su local
            String nombreLocal = usuario.getLocal(); // viene del contexto
            resultados = ventaRepository.obtenerTotalesPorCajaAbiertaYMetodoPorLocal(nombreLocal);
        }

        Map<Long, ReporteCajaResponseDTO> reporteMap = new HashMap<>();

        for (Object[] fila : resultados) {
            Long cajaId = (Long) fila[0];
            String metodoPago = (String) fila[1];
            BigDecimal total = (BigDecimal) fila[2];

            ReporteCajaResponseDTO reporte = reporteMap.computeIfAbsent(
                    cajaId,
                    id -> new ReporteCajaResponseDTO(id, BigDecimal.ZERO, new ArrayList<>()));

            reporte.detallePorMetodo.add(new DetalleCajaResponseDTO(cajaId, metodoPago, total));
            reporte.totalGeneral = reporte.totalGeneral.add(total);
        }

        return new ArrayList<>(reporteMap.values());
    }

    private List<ReporteCajaResponseDTO> listarCajasAbiertasAdmin() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (!usuario.esAdminGeneral()) {
            throw new RuntimeException("Se necesita permiso de administrador");
        }

        List<Caja> cajasAbiertas = cajaRepository.findByEstadoTrue();
        List<Object[]> resultados = ventaRepository.obtenerTotalesPorCajaAbiertaYMetodo();

        Map<Long, ReporteCajaResponseDTO> reporteMap = new HashMap<>();

        // Inicializo todas las cajas abiertas
        for (Caja c : cajasAbiertas) {
            reporteMap.put(c.getId(),
                    new ReporteCajaResponseDTO(
                            c.getId(),
                            c.getLocal().getId().toString(),
                            BigDecimal.ZERO,
                            new ArrayList<>(),
                            c.getFechaApertura(),
                            null // 👈 abiertas → fechaCierre null
                    ));
        }

        // Completo con las ventas si existen
        for (Object[] fila : resultados) {
            Long cajaId = (Long) fila[0];
            String metodoPago = (String) fila[1];
            BigDecimal total = (BigDecimal) fila[2];
            LocalDateTime fechaApertura = (LocalDateTime) fila[3];
            Long idLocal = (Long) fila[5];

            ReporteCajaResponseDTO reporte = reporteMap.get(cajaId);
            if (reporte != null) {
                reporte.getDetallePorMetodo().add(new DetalleCajaResponseDTO(cajaId, metodoPago, total));
                reporte.setTotalGeneral(reporte.getTotalGeneral().add(total));
                reporte.setFechaApertura(fechaApertura);
                reporte.setFechaCierre(null); // 👈 siempre null en abiertas
                reporte.setIdLocal(idLocal.toString());
            }
        }

        return new ArrayList<>(reporteMap.values());
    }

    public List<CajaResponseDTO> abierta() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (usuario.esAdminGeneral()) {
            return cajaRepository.findByEstado(true)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } else {
            return cajaRepository.findByLocal_NombreLocalAndEstado(usuario.getLocal(), true)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }
    }

    private CajaResponseDTO toDTO(Caja entity) {
        CajaResponseDTO dto = new CajaResponseDTO();
        dto.id = entity.getId().toString();
        dto.idUsuario = entity.getUsuario().getId().toString();
        dto.fechaApertura = entity.getFechaApertura().toString();
        dto.fechaCierre = entity.getFechaCierre() != null ? entity.getFechaCierre().toString() : null;
        dto.montoApertura = entity.getMontoApertura().toString();
        dto.estado = entity.getEstado().toString();
        dto.idLocal = entity.getLocal().getId().toString();
        return dto;
    }
}
