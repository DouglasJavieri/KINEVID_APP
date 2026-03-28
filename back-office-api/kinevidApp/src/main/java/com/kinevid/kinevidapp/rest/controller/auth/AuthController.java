package com.kinevid.kinevidapp.rest.controller.auth;

import com.kinevid.kinevidapp.rest.constants.ApiConstants;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.auth.JwtResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.LoginRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.LogoutRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.RefreshTokenRequestDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.service.auth.AuthService;
import com.kinevid.kinevidapp.rest.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 * Controlador de autenticación
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth", description = "Endpoints de autenticación y autorización")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Autenticación de usuario (Login)",
            description = "Autentica un usuario con username y password. " +
                    "Retorna Access Token (JWT corta duración) y Refresh Token (JWT larga duración).",
            tags = {"auth"},
            responses = {
                    @ApiResponse(description = "Autenticación exitosa", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
                    @ApiResponse(responseCode = "400", description = "Error en validación de datos (username o password vacíos)", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas (usuario no existe o contraseña incorrecta)", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(hidden = true)))
            }, security = {})
    public ResponseEntity<ResponseBody<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            JwtResponseDto jwtResponse = authService.login(loginRequest);

            ResponseBody<JwtResponseDto> response = ApiUtil.buildResponseWithDefaults(jwtResponse);
            response.setMessage("Login exitoso. Tokens generados.");

            log.info("Login exitoso para usuario: {}", loginRequest.getUsername());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (OperationException e) {
            log.warn("Error operacional en login para usuario: {} - {}",
                    loginRequest.getUsername(), e.getMessage());
            throw ApiResponseException.unauthorized(e.getMessage());

        } catch (Exception e) {
            log.error("Error inesperado en login para usuario: {}", loginRequest.getUsername(), e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/refresh")
    @Operation(
            summary = "Renovar Access Token",
            description = "Usa el Refresh Token para obtener un nuevo Access Token. " +
                    "Requiere un Access Token válido en el header Authorization. " +
                    "El Refresh Token debe estar válido (no revocado, no expirado).",
            tags = {"auth"},
            responses = {
                    @ApiResponse(description = "Access Token renovado exitosamente", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
                    @ApiResponse(responseCode = "400", description = "Error en validación de datos (Refresh Token vacío)", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Refresh Token inválido, expirado o revocado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(hidden = true)))
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<JwtResponseDto>> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        try {
            JwtResponseDto jwtResponse = authService.refreshAccessToken(refreshTokenRequest);

            ResponseBody<JwtResponseDto> response = ApiUtil.buildResponseWithDefaults(jwtResponse);
            response.setMessage("Access Token renovado exitosamente.");

            log.info("Access Token renovado exitosamente");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (OperationException e) {
            log.warn("Error operacional en refresh token: {}", e.getMessage());
            throw ApiResponseException.unauthorized(e.getMessage());

        } catch (Exception e) {
            log.error("Error inesperado en refresh token", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Cerrar sesión (Logout)",
            description = "Revoca el Refresh Token, invalidando el acceso hasta un nuevo login. " +
                    "Requiere un Access Token válido en el header Authorization.",
            tags = {"auth"},
            responses = {
                    @ApiResponse(description = "Logout exitoso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseBody.class))),
                    @ApiResponse(responseCode = "400", description = "Error en validación de datos (Refresh Token vacío)", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autenticado o Access Token inválido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(hidden = true)))
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Void>> logout(@Valid @RequestBody LogoutRequestDto logoutRequest) {
        try {
            authService.logout(logoutRequest.getRefreshToken());
            ResponseBody<Void> response = ResponseBody.<Void>builder()
                    .code(ApiConstants.OK_CODE)
                    .message("Logout exitoso. Sesión cerrada.")
                    .data(null)
                    .build();

            log.info("Logout exitoso - Token revocado");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (OperationException e) {
            log.warn("Error operacional en logout: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());

        } catch (Exception e) {
            log.error("Error inesperado en logout", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }
}