package com.kinevid.kinevidapp.rest.model.dto.u;

import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 01/04/2026
 * DTO para cambiar el estado de un usuario.
 */
@Data
public class ChangeUserStatusRequestDTO {

    @NotNull(message = "El estado del usuario es requerido")
    private UserStatus status;
}

