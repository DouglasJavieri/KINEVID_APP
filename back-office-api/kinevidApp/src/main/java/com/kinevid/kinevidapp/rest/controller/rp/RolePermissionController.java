package com.kinevid.kinevidapp.rest.controller.rp;

import com.kinevid.kinevidapp.rest.constants.ApiConstants;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionResponseDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.service.rp.RolePermissionService;
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

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/role-permission")
@Tag(name = "role-permission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping("/assign")
    @Operation(
            summary = "Asignar permiso a rol",
            tags = {"role-permission"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro creado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerToken")
    )
    public ResponseEntity<ResponseBody<RolePermissionResponseDto>> assignPermissionToRole(
            @Valid @RequestBody RolePermissionRequestDto request) {
        try {
            RolePermissionResponseDto assigned = rolePermissionService.assignPermissionToRole(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiUtil.buildResponseWithDefaults(assigned));

        } catch (OperationException e) {
            log.warn("Error al asignar permiso: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al asignar permiso", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/{roleId}/{permissionId}")
    @Operation(
            summary = "Remover permiso de rol",
            tags = {"role-permission"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro creado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerToken")
    )
    public ResponseEntity<ResponseBody<Boolean>> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        try {
            rolePermissionService.removePermissionFromRole(roleId, permissionId);
            return ok(ApiUtil.buildResponseWithDefaults(true));

        } catch (OperationException e) {
            log.warn("Error al remover permiso: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al remover permiso", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }
}