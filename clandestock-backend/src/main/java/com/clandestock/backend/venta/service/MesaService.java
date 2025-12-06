package com.clandestock.backend.venta.service;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.MesaRequestDTO;
import com.clandestock.backend.venta.dto.MesaResponseDTO;
import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.modelos.Mesa;
import com.clandestock.backend.venta.repository.LocalRepository;
import com.clandestock.backend.venta.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final LocalRepository localRepository;

    public MesaResponseDTO crearMesa(MesaRequestDTO request) {

        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        // Se verifica si es admin general y obtiene todas
        if (usuario.esAdminGeneral()) {
            throw new RuntimeException("Administrador general no puede manejar mesas");

            // Caso contario obtiene la corresponiende al local asignado
        } else {
            Local local = localRepository.findByNombreLocal(usuario.getLocal())
                    .orElseThrow(() -> new RuntimeException("Error obteniendo local"));
            Mesa mesa = toEntity(request, local);
            Mesa guardada = mesaRepository.save(mesa);
            return toResponse(guardada);
        }
    }

    public List<MesaResponseDTO> listarMesasPorLocal() {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Mesa> mesas;

        if (usuario.esAdminGeneral()) {
            mesas = mesaRepository.findAll();
        } else {
            mesas = mesaRepository.findByLocal_NombreLocal(usuario.getLocal());
        }

        return mesas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Mesa toEntity(MesaRequestDTO request, Local local) {
        Mesa mesa = Mesa.builder()
                .numeroMesa(request.getNumeroMesa())
                .ocupada(request.getOcupada() != null ? request.getOcupada() : false)
                .local(local)
                .build();

        return mesa;
    }

    private MesaResponseDTO toResponse(Mesa mesa) {
        return MesaResponseDTO.builder()
                .id(mesa.getId())
                .numeroMesa(mesa.getNumeroMesa())
                .ocupada(mesa.getOcupada())
                .localId(mesa.getLocal().getId())
                .build();
    }

    public MesaResponseDTO actualizar(MesaRequestDTO request, Long id) {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        // Se verifica si es admin general y obtiene todas
        if (usuario.esAdminGeneral()) {
            throw new RuntimeException("Administrador general no puede manejar mesas");

            // Caso contario obtiene la corresponiende al local asignado
        } else {
            Local local = localRepository.findByNombreLocal(usuario.getLocal())
                    .orElseThrow(() -> new RuntimeException("Error obteniendo local"));
            Mesa mesa = mesaRepository .findByIdAndLocal(id, local).orElseThrow(()->new RuntimeException("Error obteniendo mesa por id y local"));
            mesa.setNumeroMesa(request.getNumeroMesa());;
            mesa.setOcupada(request.getOcupada());
            Mesa guardada = mesaRepository.save(mesa);
            return toResponse(guardada);
        }
    }
}
