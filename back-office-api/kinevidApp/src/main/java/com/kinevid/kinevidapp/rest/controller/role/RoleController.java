package com.kinevid.kinevidapp.rest.controller.role;

import com.kinevid.kinevidapp.rest.constants.ApiConstants;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.role.PagedRoleResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.role.RoleResponseDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.service.role.RoleService;
import com.kinevid.kinevidapp.rest.util.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/role")
@Tag(name = "role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo registro role",
            description = "Registra un nuevo role en el sistema",
            tags = {"role"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro creado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<RoleResponseDto>> createRole(@RequestBody RoleResponseDto role) {
        try {
            RoleResponseDto roleModel = roleService.createRole(role);
            return ok(ApiUtil.buildResponseWithDefaults(roleModel));
        } catch (OperationException e) {
            log.error("Error: Se produjo un error controlado al ejecutar el servicio, Mensaje: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Se produjo un error genérico al ejecutar el servicio: ", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{idRol}")
    @Operation(summary = "Actualizar un nuevo registro role",
            description = "Actualizar un nuevo role en el sistema",
            tags = {"role"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(description = "Registro actualizado", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<RoleResponseDto>> updateRole(@RequestBody RoleResponseDto role,
                                                                    @PathVariable("idRol") Long idRol) {
        try {
            RoleResponseDto roleModel = roleService.updateRole(idRol, role);
            return ok(ApiUtil.buildResponseWithDefaults(roleModel));
        } catch (OperationException e) {
            log.error("Error: Se produjo un error controlado al ejecutar el servicio, Mensaje: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Se produjo un error genérico al ejecutar el servicio: ", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{idRol}")
    @Operation(summary = "Eliminar rol",
            description = "Realiza una eliminación lógica del rol cambiando su estado a ELIMINATION y marcando deleted como true.",
            tags = {"role"},
            responses = {
                    @ApiResponse(description = "Rol eliminado con éxito", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error en la validación de datos", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Boolean>> deleteRole(@PathVariable("idRol") Long idRol) {
        try {
            this.roleService.deleteRole(idRol);
            return ok(ApiUtil.buildResponseWithDefaults(true));
        } catch (OperationException e) {
            log.error("Error: Operación controlada al eliminar usuario con ID {}: {}", idRol, e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Excepción genérica al eliminar usuario con ID {}: ", idRol, e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/page-role")
    @Operation(
            summary = "Lista paginada de rol",
            description = "Metodo para listar a los roles de manera paginada",
            tags = {"role"},
            responses = {
                    @ApiResponse(description = "Operación satisfactoria", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Page<PagedRoleResponseDto>>> pagedRole(@RequestParam("page") int page,
                                                                          @RequestParam("size") int size,
                                                                          @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                          @RequestParam(value = "sortDir", defaultValue = "DESC") Sort.Direction sortDir,
                                                                          @RequestParam(value = "status", required = false) String status) {
        try {
            Pageable pageable = ApiUtil.buildPageableWithSort(page, size, sortBy, sortDir);
            Page<PagedRoleResponseDto> pageList = roleService.findAllRoles(status, pageable);
            return ok(ApiUtil.buildResponseWithDefaults(pageList));
        } catch (OperationException e) {
            log.error("Error: Se produjo un error controlado al ejecutar el servicio, Mensaje: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Se produjo un error genérico al ejecutar el servicio: ", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/change-status/{idRol}/{status}")
    @Operation(summary = "Cambia estado de rol",
            description = "Metodo para cambiar el estado del rol",
            tags = {"role"},
            responses = {
                    @ApiResponse(description = "Operación satisfactorio", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Fallo de autentificación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso Denegado", content = @Content(schema = @Schema(hidden = true))),
            }, security = @SecurityRequirement(name = "bearerToken"))
    public ResponseEntity<ResponseBody<Boolean>> listPositionByBusinessUnit(@PathVariable("idRol") Long idRol,
                                                                            @PathVariable("status") String status) {
        try {
            this.roleService.changeStatus(idRol, status);
            return ok(ApiUtil.buildResponseWithDefaults(true));
        } catch (OperationException e) {
            log.error("Error: Se produjo un error controlado al ejecutar el servicio, Mensaje: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error: Se produjo un error genérico al ejecutar el servicio: ", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }
}
