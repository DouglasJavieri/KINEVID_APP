package com.kinevid.kinevidapp.rest.model.dto.role;

import com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 01/04/2026
 * DTO para cambio de estado de un rol.
 */
@Data
public class ChangeRoleStatusRequestDTO {

    @NotNull(message = "El estado del rol es requerido")
    private RoleStatus status;
}

