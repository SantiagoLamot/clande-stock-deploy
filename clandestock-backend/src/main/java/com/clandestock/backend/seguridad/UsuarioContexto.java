package com.clandestock.backend.seguridad;

import com.clandestock.backend.usuario.modelos.TipoUsuarioEnum;

public class UsuarioContexto {
    private final String nombreUsuario;
    private final TipoUsuarioEnum tipoUsuario;

    public UsuarioContexto(String nombreUsuario, TipoUsuarioEnum tipoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean esAdminGeneral() {
        return tipoUsuario == TipoUsuarioEnum.ADMIN_GENERAL;
    }

    public String getLocal() {
        return tipoUsuario.getLocal();
    }
}