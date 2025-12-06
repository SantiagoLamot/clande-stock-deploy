package com.clandestock.backend.venta.service;

import com.clandestock.backend.auth.dto.RegistroRequest;
import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.usuario.modelos.Usuario;
import com.clandestock.backend.usuario.repository.UsuarioRepository;
import com.clandestock.backend.venta.dto.ReporteRequest;
import com.clandestock.backend.venta.dto.ReporteResponse;
import com.clandestock.backend.venta.modelos.Reporte;
import com.clandestock.backend.venta.repository.ReporteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {
    private ReporteRepository reporteRepository;
    private UsuarioRepository usuarioRepository;

    public ReporteService(ReporteRepository reporteRepository, UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ReporteResponse cargarReporte (ReporteRequest request){
        Reporte reporte = toEntity(request);
        reporte.setEstado(false);
        Reporte reporteGuardado = reporteRepository.save(reporte);
        return toResponse(reporteGuardado);
    }

    public List<ReporteResponse> obtenerReportesPorContexto(UsuarioContexto usuario) {
        if (usuario.esAdminGeneral()) {
            // Admin general ve todos los reportes
            return reporteRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
        } else {
            // Moderador: traer solo los reportes de SU usuario
            Usuario usuarioEmisor = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            return reporteRepository.findByUsuarioEmisor_Id(usuarioEmisor.getId())
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }
    }

    public Reporte toEntity(ReporteRequest request) {
        Usuario usuarioEmisor = usuarioRepository.findByNombreUsuario(request.usuarioEmisor())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuario emisor " + request.usuarioEmisor() + " inexistente"
                ));

        return Reporte.builder()
                .descripcion(request.descripcion())
                .usuarioEmisor(usuarioEmisor)
                .estado(false)
                .fecha(LocalDateTime.now())
                .build();
    }


    private ReporteResponse toResponse(Reporte reporte) {
        return new ReporteResponse(
                reporte.getId(),
                reporte.getDescripcion(),
                reporte.getUsuarioEmisor().getNombreUsuario(),
                reporte.getEstado() ? "Leído" : "No leído",
                reporte.getFecha().toString()
        );
    }


    public ReporteResponse checkReporte (Long idReporte) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new EntityNotFoundException("Reporte con ID inexistente"));
        reporte.setEstado(true);
        Reporte reporteCheck = reporteRepository.save(reporte);
        return toResponse(reporteCheck);
    }

    public void deleteReporte (Long idReporte) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(()-> new EntityNotFoundException("Reporte no existente"));
        reporteRepository.delete(reporte);
    }
}
