package com.clandestock.backend.usuario.modelos;

public enum TipoUsuarioEnum {
    ADMIN_GENERAL(null),
    MODERADOR_TENEDOR_LIBRE("tenedor_libre"),
    MODERADOR_TERMAS("termas"),
    MODERADOR_HELADERIA("heladeria");

    private final String local;

    TipoUsuarioEnum(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public boolean esAdminGeneral() {
        return this == ADMIN_GENERAL;
    }

    public boolean esModerador() {
        return !esAdminGeneral();
    }

    public boolean puedeVerLocal(String localSolicitado) {
        return esAdminGeneral() || (local != null && local.equalsIgnoreCase(localSolicitado));
    }

    @Override
    public String toString() {
        return local != null ? local : "admin_general";
    }
}