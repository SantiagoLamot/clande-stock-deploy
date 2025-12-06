package com.clandestock.backend.venta.service;

import com.clandestock.backend.venta.modelos.Local;
import com.clandestock.backend.venta.repository.LocalRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalService {
    private LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public Local obtenerPorId(Long id){
        return localRepository.findById(id).orElseThrow(()-> new RuntimeException("Local no encontrado"));
    }

    public Local obtenerPorNombre(String nombreLocal){
        return localRepository.findByNombreLocal(nombreLocal).orElseThrow(()-> new RuntimeException("Local no encontrado"));
    }
}
