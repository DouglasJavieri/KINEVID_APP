package com.kinevid.kinevidapp.rest.model.dto.auth;

import lombok.Data;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
