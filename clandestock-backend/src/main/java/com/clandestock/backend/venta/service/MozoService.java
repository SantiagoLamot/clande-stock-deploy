package com.clandestock.backend.venta.service;

import com.clandestock.backend.seguridad.UsuarioContexto;
import com.clandestock.backend.venta.dto.MesaRequestDTO;
import com.clandestock.backend.venta.dto.MozoRequestDTO;
import com.clandestock.backend.venta.dto.MozoResponseDTO;
import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.modelos.Mesa;
import com.clandestock.backend.venta.modelos.Mozo;
import com.clandestock.backend.venta.repository.MozoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MozoService {

    private final MozoRepository mozoRepository;
    private final LocalService localService;

    public MozoResponseDTO crearMozo(MozoRequestDTO dto, UsuarioContexto usuarioContexto) {
        if (usuarioContexto.esAdminGeneral()) {
            throw new RuntimeException("El administrador general no puede crear mozos directamente");
        }

        Local local = localService.obtenerPorNombre(usuarioContexto.getLocal());

        Mozo mozo = Mozo.builder()
                .nombre(dto.getNombre())
                .local(local)
                .build();

        mozo = mozoRepository.save(mozo);

        return toResponseDTO(mozo);
    }

    public List<MozoResponseDTO> listarMozos(UsuarioContexto usuarioContexto) {
        List<Mozo> mozos;

        if (usuarioContexto.esAdminGeneral()) {
            mozos = mozoRepository.findAll();
        } else {
            mozos = mozoRepository.findByLocal_NombreLocal(usuarioContexto.getLocal());
        }

        return mozos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private MozoResponseDTO toResponseDTO(Mozo mozo) {
        return MozoResponseDTO.builder()
                .id(mozo.getId())
                .nombre(mozo.getNombre())
                .localId(mozo.getLocal().getId())
                .nombreLocal(mozo.getLocal().getNombreLocal())
                .build();
    }

    public MozoResponseDTO eliminarMozo(Long idMozo, UsuarioContexto usuarioContexto) {
        Mozo mozo = mozoRepository.findById(idMozo)
                .orElseThrow(() -> new RuntimeException("Mozo con ID " + idMozo + " no encontrado"));

        // Validación de permisos
        if (usuarioContexto.esAdminGeneral()) {
            // Admin general puede eliminar cualquier mozo
        } else {
            // Moderador solo puede eliminar mozos de su propio local
            if (!mozo.getLocal().getNombreLocal().equals(usuarioContexto.getLocal())) {
                throw new RuntimeException("No tiene permisos para eliminar mozos de otro local");
            }
        }

        mozoRepository.delete(mozo);

        return MozoResponseDTO.builder()
                .id(mozo.getId())
                .nombre(mozo.getNombre())
                .localId(mozo.getLocal().getId())
                .nombreLocal(mozo.getLocal().getNombreLocal())
                .build();
    }

    public MozoResponseDTO actualizar(Long id, MozoRequestDTO request) {
        UsuarioContexto usuario = (UsuarioContexto) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        // Se verifica si es admin general y obtiene todas
        if (usuario.esAdminGeneral()) {
            throw new RuntimeException("Administrador general no actualizar mozos");

            // Caso contario obtiene la corresponiende al local asignado
        } else {
            Local local = localService.obtenerPorNombre(usuario.getLocal());
            Mozo mozo = mozoRepository.findByIdAndLocal(id, local).orElseThrow(()->new RuntimeException("Error obteniendo mesa por id y local"));
            mozo.setNombre(request.getNombre());
            Mozo mozoGuardado = mozoRepository.save(mozo);
            return toResponseDTO(mozoGuardado);
        }
    }

}

