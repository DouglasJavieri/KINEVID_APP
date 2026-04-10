package com.kinevid.kinevidapp.rest.model.dto.emp;

import com.kinevid.kinevidapp.rest.model.enums.emp.EmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 07/04/2026
 * DTO para cambiar el estado de un empleado.
 */
@Data
public class ChangeEmployeeStatusRequestDTO {

    @NotNull(message = "El estado del empleado es requerido")
    private EmployeeStatus status;
}

