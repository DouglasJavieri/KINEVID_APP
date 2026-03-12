package com.kinevid.kinevidapp.rest.model.dto.u;

import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import lombok.Data;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
}
