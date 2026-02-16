package com.kinevid.kinevidapp.rest.controller.auth;

import com.kinevid.kinevidapp.config.security.JwtUtils;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.model.dto.auth.JwtResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.LoginRequestDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Autenticación de usuario",
            description = "Permite autenticarse y obtener un JWT",
            responses = {
                @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
    }, security = {}
    )
    public ResponseEntity<ResponseBody<JwtResponseDto>> login(@RequestBody LoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));

            String jwt = jwtUtils.generateJwtToken(authentication);

            JwtResponseDto response = new JwtResponseDto(jwt);

            return ResponseEntity.ok(ApiUtil.buildResponseWithDefaults(response));

        } catch (BadCredentialsException e) {
            throw ApiResponseException.badRequest("Credenciales inválidas");
        } catch (Exception e) {
            throw ApiResponseException.serverError("Error interno de autenticación");
        }
    }

}
