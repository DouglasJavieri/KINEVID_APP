package com.kinevid.kinevidapp.rest.service.auth;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.auth.JwtResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.LoginRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.RefreshTokenRequestDto;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 27/03/2026
 * Interfaz de servicio de autenticación
 */
public interface AuthService {

    /**
     * Autentica un usuario con username y password
     * Genera Access Token y Refresh Token
     */
    JwtResponseDto login(LoginRequestDto loginRequest) throws OperationException;

    /**
     * Renueva el Access Token usando el Refresh Token
     * El Refresh Token debe ser válido y no estar revocado
     */
    JwtResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequest) throws OperationException;

    /**
     * Realiza logout revocando el Refresh Token
     * El usuario deberá loguearse nuevamente para obtener nuevos tokens
     */
    void logout(String refreshToken) throws OperationException;

    /**
     * Obtiene el usuario autenticado actualmente
     */
    User getCurrentUser() throws OperationException;
}