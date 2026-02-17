package com.kinevid.kinevidapp.rest.controller.u;

import com.kinevid.kinevidapp.rest.constants.ApiConstants;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.u.UserRequestDTO;
import com.kinevid.kinevidapp.rest.model.dto.u.UserResponseDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.service.u.UserService;
import com.kinevid.kinevidapp.rest.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 17/02/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Tag(name = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo registro usuario",
            description = "Registra un nuevo usuario en el sistema",
            tags = {"users"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro creado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<UserResponseDto>> createUser(@RequestBody UserRequestDTO user) {
        try {
            UserResponseDto createdUser = userService.createUser(user);
            return ok(ApiUtil.buildResponseWithDefaults(createdUser));

        } catch (OperationException e) {
            log.error("Error: Se produjo un error controlado al ejecutar el servicio, Mensaje: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Se produjo un error genérico al ejecutar el servicio: ", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente validando que el username y email no estén duplicados.",
            tags = {"users"},
            responses = {
                    @ApiResponse(description = "Usuario actualizado con éxito", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error en la validación de datos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<UserResponseDto>> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO user) {
        try {
            UserResponseDto updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(ApiUtil.buildResponseWithDefaults(updatedUser));

        } catch (OperationException e) {
            log.error("Error: Operación controlada al actualizar usuario con ID {}: {}", id, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Excepción genérica al actualizar usuario con ID {}: ", id, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar usuario",
            description = "Realiza una eliminación lógica del usuario cambiando su estado a ELIMINATION y marcando deleted como true.",
            tags = {"users"},
            responses = {
                    @ApiResponse(description = "Usuario eliminado con éxito", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error en la validación de datos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Boolean>> deleteUser(@PathVariable Long id) {
        try {
            this.userService.deleteUser(id);
            return ok(ApiUtil.buildResponseWithDefaults(true));

        } catch (OperationException e) {
            log.error("Error: Operación controlada al eliminar usuario con ID {}: {}", id, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Excepción genérica al eliminar usuario con ID {}: ", id, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }
}
