package com.clandestock.backend.venta.service;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.MetodoPagoRequestDTO;
import com.clandestock.backend.venta.dto.MetodoPagoResponseDTO;
import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.modelos.MetodoPago;
import com.clandestock.backend.venta.repository.LocalRepository;
import com.clandestock.backend.venta.repository.MetodoPagoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetodoPagoService {
    private MetodoPagoRepository metodoPagoRepository;
    private LocalRepository localRepository;
    private LocalService localService;

    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository,
                             LocalRepository localRepository,
                             LocalService localService) {
        this.metodoPagoRepository = metodoPagoRepository;
        this.localRepository = localRepository;
        this.localService = localService;
    }

    private MetodoPago obtenerMetodoPagoPorID(Long id) {
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Metodo de pago con ID " + id + " no encontrado."));
        return metodoPago;
    }

    public MetodoPagoResponseDTO insertarMetodoPago(MetodoPagoRequestDTO dto) {
        MetodoPago metodoPago = toEntity(dto);

        if (metodoPago.getIncremento() > 0 && metodoPago.getDescuento() > 0) {
            throw new RuntimeException("Solo se debe cargar descuento o incremento, no los dos juntos.");
        }

        MetodoPago metodoPagoGuardado = metodoPagoRepository.save(metodoPago);
        return toDTO(metodoPagoGuardado);
    }

    public MetodoPagoResponseDTO actualizarMetodoPago(MetodoPagoRequestDTO dto) {
        MetodoPago metodoPagoInicial = obtenerMetodoPagoPorID(Long.parseLong(dto.id()));

        metodoPagoInicial.setNombreMetodoPago(dto.nombre_metodo_pago());
        metodoPagoInicial.setDescuento(Long.parseLong(dto.descuento()));
        metodoPagoInicial.setIncremento(Long.parseLong(dto.incremento()));
        metodoPagoInicial.setEstado(Boolean.parseBoolean(dto.estado()));
        if (dto.local_id() != null) {
            Local local = localRepository.findById(Long.parseLong(dto.local_id()))
                    .orElseThrow(() -> new EntityNotFoundException(
                            "El id del local al que se quiere actualizar, no existe"));
            metodoPagoInicial.setLocal(local);
        }

        MetodoPago metodoPagoGuardado = metodoPagoRepository.save(metodoPagoInicial);
        return toDTO(metodoPagoGuardado);
    }

    public MetodoPagoResponseDTO bajaLogica(Long id) {
        MetodoPago metodoPago = obtenerMetodoPagoPorID(id);
        metodoPago.setEstado(false);
        MetodoPago metodoPagoActualizado = metodoPagoRepository.save(metodoPago);
        return toDTO(metodoPagoActualizado);
    }

    public MetodoPagoResponseDTO altaLogica(Long id) {
        MetodoPago metodoPago = obtenerMetodoPagoPorID(id);
        metodoPago.setEstado(true);
        MetodoPago metodoPagoActualizado = metodoPagoRepository.save(metodoPago);
        return toDTO(metodoPagoActualizado);
    }

    public List<MetodoPagoResponseDTO> listarMetodosPagoPorUsuario(UsuarioContexto usuarioContexto) {
        List<MetodoPago> metodos;

        if (usuarioContexto.esAdminGeneral()) {
            metodos = metodoPagoRepository.findAll();
        } else {
            Local local = localService.obtenerPorNombre(usuarioContexto.getLocal());
            metodos = metodoPagoRepository.findByLocal(local);
        }

        return metodos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private MetodoPagoResponseDTO toResponseDTO(MetodoPago metodo) {
        return MetodoPagoResponseDTO.builder()
                .id(String.valueOf(metodo.getId()))
                .nombre_metodo_pago(metodo.getNombreMetodoPago())
                .incremento(String.valueOf(metodo.getIncremento()))
                .descuento(String.valueOf(metodo.getDescuento()))
                .estado(String.valueOf(metodo.getEstado()))
                .local_id(String.valueOf(metodo.getLocal().getId()))
                .build();
    }


    public List<MetodoPagoResponseDTO> listarPorLocalId(Long localId) {
        List<MetodoPago> lista = metodoPagoRepository.findByLocalId(localId);

        if (lista.isEmpty()) {
            throw new RuntimeException("No se encontraron métodos de pago para el local con ID " + localId);
        }

        return lista.stream()
                .map(metodo -> new MetodoPagoResponseDTO(
                        String.valueOf(metodo.getId()),
                        metodo.getNombreMetodoPago(),
                        metodo.getIncremento() != null ? String.valueOf(metodo.getIncremento()) : null,
                        metodo.getDescuento() != null ? String.valueOf(metodo.getDescuento()) : null,
                        metodo.getEstado() != null ? String.valueOf(metodo.getEstado()) : null,
                        metodo.getLocal() != null ? String.valueOf(metodo.getLocal().getId()) : null))
                .toList();
    }

    public List<MetodoPagoResponseDTO> listarMetodosActivosPorUsuario(UsuarioContexto usuarioContexto) {
        List<MetodoPago> metodos;
        if (usuarioContexto.esAdminGeneral()) {
            metodos = metodoPagoRepository.findByEstadoTrue();
        } else {
            Local local = localService.obtenerPorNombre(usuarioContexto.getLocal());
            metodos = metodoPagoRepository.findByLocalAndEstadoTrue(local);
        }
        return metodos.stream().map(this::toResponseDTO).toList();
    }

    public List<MetodoPagoResponseDTO> listarMetodosInactivosPorUsuario(UsuarioContexto usuarioContexto) {
        List<MetodoPago> metodos;
        if (usuarioContexto.esAdminGeneral()) {
            metodos = metodoPagoRepository.findByEstadoFalse();
        } else {
            Local local = localService.obtenerPorNombre(usuarioContexto.getLocal());
            metodos = metodoPagoRepository.findByLocalAndEstadoFalse(local);
        }
        return metodos.stream().map(this::toResponseDTO).toList();
    }

    public List<MetodoPagoResponseDTO> listarActivosPorLocal(Long localId) {
        List<MetodoPago> lista = metodoPagoRepository.findByLocalIdAndEstadoTrue(localId);

        if (lista.isEmpty()) {
            throw new RuntimeException("No se encontraron métodos de pago activos para el local con ID " + localId);
        }

        return lista.stream()
                .map(metodo -> new MetodoPagoResponseDTO(
                        String.valueOf(metodo.getId()),
                        metodo.getNombreMetodoPago(),
                        metodo.getIncremento() != null ? String.valueOf(metodo.getIncremento()) : null,
                        metodo.getDescuento() != null ? String.valueOf(metodo.getDescuento()) : null,
                        metodo.getEstado() != null ? String.valueOf(metodo.getEstado()) : null,
                        metodo.getLocal() != null ? String.valueOf(metodo.getLocal().getId()) : null))
                .toList();
    }

    // CASTEOS
    // Metodo pago (Entidad) a DTO.
    public MetodoPagoResponseDTO toDTO(MetodoPago metodoPago) {
        return new MetodoPagoResponseDTO(
                String.valueOf(metodoPago.getId()),
                metodoPago.getNombreMetodoPago(),
                metodoPago.getIncremento() != null ? String.valueOf(metodoPago.getIncremento()) : null,
                metodoPago.getDescuento() != null ? String.valueOf(metodoPago.getDescuento()) : null,
                metodoPago.getEstado() != null ? String.valueOf(metodoPago.getEstado()) : null,
                metodoPago.getLocal() != null ? String.valueOf(metodoPago.getLocal().getId()) : null);
    }

    // DTO a MetodoPago (Entidad)
    public MetodoPago toEntity(MetodoPagoRequestDTO dto) {
        Local local = localRepository.findById(Long.parseLong(dto.local_id()))
                .orElseThrow(() -> new EntityNotFoundException("Local con ID " + dto.local_id() + "no encontrado"));

        return MetodoPago.builder()
                .id(dto.id() != null ? Long.parseLong(dto.id()) : null)
                .nombreMetodoPago(dto.nombre_metodo_pago())
                .incremento(dto.incremento() != null ? Long.parseLong(dto.incremento()) : null)
                .descuento(dto.descuento() != null ? Long.parseLong(dto.descuento()) : null)
                .estado(dto.estado() != null ? Boolean.parseBoolean(dto.estado()) : null)
                .local(local)
                .build();
    }
}
