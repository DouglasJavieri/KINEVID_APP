package com.kinevid.kinevidapp.rest.service.auth.impl;

import com.kinevid.kinevidapp.config.security.JwtUtils;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.auth.JwtResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.LoginRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.RefreshTokenRequestDto;
import com.kinevid.kinevidapp.rest.model.entity.auth.RefreshToken;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.repository.u.UserRepository;
import com.kinevid.kinevidapp.rest.service.auth.AuthService;
import com.kinevid.kinevidapp.rest.service.auth.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 27/03/2026
 * Implementación del servicio de autenticación
 * Gestiona login, refresh token y logout
 * Utiliza RefreshTokenService para la gestión de Refresh Tokens
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * Autentica un usuario con username y password
     */
    @Override
    @Transactional
    public JwtResponseDto login(LoginRequestDto loginRequest) throws OperationException {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());

        try {
            // Paso 1: Autenticar credenciales contra la BD
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Paso 2: Establecer autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Paso 3: Cargar usuario de la BD
            User user = userRepository.findByUsernameAuthentication(loginRequest.getUsername())
                    .orElseThrow(() -> new OperationException("Usuario no encontrado"));

            // Paso 4: Generar Access Token (corta duración)
            String accessToken = jwtUtils.generateAccessToken(user.getUsername());
            long accessTokenExpiresIn = jwtUtils.getAccessTokenExpirationTime();

            // Paso 5: Crear y guardar Refresh Token en BD
            // RefreshTokenService genera el JWT Y lo asigna a la entidad
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, null, null);

            log.info("Login exitoso para usuario: {}", user.getUsername());

            // Paso 6: Construir respuesta
            return JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(accessTokenExpiresIn)
                    .userInfo(JwtResponseDto.UserAuthInfoDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .fullName(user.getUsername())
                            .role("USER")
                            .build()
                    )
                    .build();

        } catch (org.springframework.security.core.AuthenticationException e) {
            log.warn("Login fallido para usuario: {} - {}",
                    loginRequest.getUsername(), e.getMessage());
            throw new OperationException("Credenciales inválidas. Verifique usuario y contraseña");
        } catch (OperationException e) {
            log.warn("Error operacional en login: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error genérico en login para usuario: {}", loginRequest.getUsername(), e);
            throw new OperationException("Error durante el login. Intente nuevamente");
        }
    }

    /**
     * Renueva el Access Token usando el Refresh Token
     */
    @Override
    @Transactional
    public JwtResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequest) throws OperationException {
        log.info("Intento de renovación de Access Token");

        try {
            String refreshTokenValue = refreshTokenRequest.getRefreshToken();

            // Paso 1: Validar el JWT del Refresh Token usando JwtUtils
            if (!jwtUtils.validateRefreshToken(refreshTokenValue)) {
                log.warn("Refresh Token JWT inválido o expirado");
                throw new OperationException("Refresh Token inválido o expirado");
            }

            // Paso 2: Validar el Refresh Token en BD usando RefreshTokenService
            if (!refreshTokenService.validateRefreshToken(refreshTokenValue)) {
                log.warn("Refresh Token no válido en BD (revocado o expirado)");
                throw new OperationException("Refresh Token no válido");
            }

            // Paso 3: Extraer username del Refresh Token
            String username = jwtUtils.getUsernameFromRefreshToken(refreshTokenValue);

            // Paso 4: Cargar usuario
            User user = userRepository.findByUsernameAuthentication(username)
                    .orElseThrow(() -> new OperationException("Usuario no encontrado"));

            // Paso 5: Generar nuevo Access Token
            String newAccessToken = jwtUtils.generateAccessToken(user.getUsername());
            long accessTokenExpiresIn = jwtUtils.getAccessTokenExpirationTime();

            log.info("Access Token renovado para usuario: {}", username);

            // Paso 6: Construir respuesta (sin nuevo Refresh Token)
            return JwtResponseDto.builder()
                    .accessToken(newAccessToken)
                    .tokenType("Bearer")
                    .expiresIn(accessTokenExpiresIn)
                    .userInfo(JwtResponseDto.UserAuthInfoDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .fullName(user.getUsername())
                            .role("USER")
                            .build()
                    )
                    .build();

        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al renovar Access Token", e);
            throw new OperationException("Error al renovar el token. Intente login nuevamente");
        }
    }

    /**
     * Realiza logout revocando el Refresh Token
     */
    @Override
    @Transactional
    public void logout(String refreshToken) throws OperationException {
        log.info("Intento de logout");

        try {
            refreshTokenService.revokeToken(refreshToken);

            log.info("Logout exitoso - Refresh Token revocado");

        } catch (OperationException e) {

            log.warn("Error al revocar Refresh Token en logout: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al realizar logout", e);
        }
    }

    /**
     * Obtiene el usuario autenticado actualmente del contexto de seguridad
     */
    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() throws OperationException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new OperationException("Usuario no autenticado");
            }

            String username = authentication.getName();
            return userRepository.findByUsernameAuthentication(username)
                    .orElseThrow(() -> new OperationException("Usuario no encontrado"));
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener usuario autenticado", e);
            throw new OperationException("Error al obtener información del usuario");
        }
    }
}