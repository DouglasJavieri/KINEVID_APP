package com.kinevid.kinevidapp.rest.model.enums.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 01/02/2026
 */
@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVO("ACTIVO", "Activo"),
    INACTIVO("INACTIVO", "Inactivo"),
    BLOQUEADO("BLOQUEADO", "Bloqueado"),
    ELIMINADO("ELIMINADO", "Eliminado");

    private final String value;
    private final String description;

    @Override
    public String toString() {return value;}
}
