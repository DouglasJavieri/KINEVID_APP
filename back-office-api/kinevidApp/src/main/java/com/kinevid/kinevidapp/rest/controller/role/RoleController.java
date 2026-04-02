package com.kinevid.kinevidapp.rest.controller.role;

import com.kinevid.kinevidapp.rest.constants.ApiConstants;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.role.ChangeRoleStatusRequestDTO;
import com.kinevid.kinevidapp.rest.model.dto.role.PagedRoleResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.role.RoleRequestDTO;
import com.kinevid.kinevidapp.rest.model.dto.role.RoleResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.u.UserResponseDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.response.ResponsePage;
import com.kinevid.kinevidapp.rest.service.role.RoleService;
import com.kinevid.kinevidapp.rest.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/role")
@Tag(name = "roles", description = "Gestión de roles del sistema")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @Operation(summary = "Crear un nuevo rol",
            description = "Registra un nuevo rol en el sistema. El nombre se normaliza a MAYÚSCULAS. Requiere permiso CREATE_ROLE.",
            tags = {"roles"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro creado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<RoleResponseDto>> createRole(@Valid @RequestBody RoleRequestDTO role) {
        try {
            RoleResponseDto roleModel = roleService.createRole(role);
            return ok(ApiUtil.buildResponseWithDefaults(roleModel));
        } catch (OperationException e) {
            log.error("Error al crear rol: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al crear rol", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ROLE')")
    @Operation(summary = "Obtener rol por ID",
            description = "Retorna los datos de un rol activo por su ID. Requiere permiso READ_ROLE.",
            tags = {"roles"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro actualizado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<RoleResponseDto>> getRoleById(@PathVariable Long id) {
        try {
            RoleResponseDto role = roleService.getRoleById(id);
            return ok(ApiUtil.buildResponseWithDefaults(role));
        } catch (OperationException e) {
            log.error("Error al obtener rol con ID {}: {}", id, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al obtener rol con ID {}", id, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('LIST_ROLE')")
    @Operation(summary = "Listar roles con paginación",
            description = "Retorna roles activos paginados. El filtro 'status' es opcional (ACTIVE, INACTIVE). Requiere permiso LIST_ROLE.",
            tags = {"roles"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Estado inválido", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Sin permiso LIST_ROLE", content = @Content(schema = @Schema(hidden = true)))
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Page<PagedRoleResponseDto>>> getAllRoles(@RequestParam("page") int page,
                                                                          @RequestParam("size") int size,
                                                                          @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                          @RequestParam(value = "sortDir", defaultValue = "DESC") Sort.Direction sortDir,
                                                                          @RequestParam(required = false) String status) {
        try {
            Pageable pageable = ApiUtil.buildPageableWithSort(page, size, sortBy, sortDir);
            Page<PagedRoleResponseDto> usersPage = roleService.findAllRoles(status, pageable);
            return ok(ApiUtil.buildResponseWithDefaults(usersPage));
        } catch (OperationException e) {
            log.error("Error al listar roles: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al listar roles", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @Operation(summary = "Actualizar rol",
            description = "Actualiza nombre y descripción de un rol existente. Requiere permiso UPDATE_ROLE.",
            tags = {"roles"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Validación fallida o nombre duplicado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Sin permiso UPDATE_ROLE", content = @Content(schema = @Schema(hidden = true)))
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<RoleResponseDto>> updateRole(@PathVariable(value = "id") Long id, @Valid @RequestBody RoleRequestDTO role) {
        try {
            RoleResponseDto updated = roleService.updateRole(id, role);
            return ok(ApiUtil.buildSuccessResponse(updated, "Rol actualizado exitosamente."));
        } catch (OperationException e) {
            log.error("Error al actualizar rol con ID {}: {}", id, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al actualizar rol con ID {}", id, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @Operation(summary = "Cambiar estado de rol",
            description = "Cambia el estado entre ACTIVE e INACTIVE. " +
                    "Para eliminar use DELETE. No permite estado ELIMINATION. Requiere permiso UPDATE_ROLE.",
            tags = {"roles"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Estado inválido o rol no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Sin permiso UPDATE_ROLE", content = @Content(schema = @Schema(hidden = true)))
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<RoleResponseDto>> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleStatusRequestDTO request) {
        try {
            RoleResponseDto updated = roleService.changeStatus(id, request.getStatus());
            return ok(ApiUtil.buildSuccessResponse(updated, "Estado del rol cambiado exitosamente."));
        } catch (OperationException e) {
            log.error("Error al cambiar estado del rol con ID {}: {}", id, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al cambiar estado del rol con ID {}", id, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @Operation(summary = "Eliminar rol (lógico)",
            description = "Eliminación lógica del rol. No permite eliminar ROLE_ADMIN ni roles con usuarios activos. Requiere permiso DELETE_ROLE.",
            tags = {"roles"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Rol no encontrado, protegido o con usuarios activos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Sin permiso DELETE_ROLE", content = @Content(schema = @Schema(hidden = true)))
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Boolean>> deleteRole(@PathVariable(value = "id") Long id) {
        try {
            roleService.deleteRole(id);
            return ok(ApiUtil.buildSuccessResponse(true, "Rol eliminado exitosamente."));
        } catch (OperationException e) {
            log.error("Error al eliminar rol con ID {}: {}", id, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al eliminar rol con ID {}", id, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }
}
