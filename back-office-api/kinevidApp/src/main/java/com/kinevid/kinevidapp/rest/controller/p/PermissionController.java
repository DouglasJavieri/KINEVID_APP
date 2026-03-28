package com.kinevid.kinevidapp.rest.controller.p;

import com.kinevid.kinevidapp.rest.constants.ApiConstants;
import com.kinevid.kinevidapp.rest.exception.ApiResponseException;
import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.p.PermissionResponseDto;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import com.kinevid.kinevidapp.rest.service.p.PermissionService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/permission")
@Tag(name = "permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/create")
    @Operation(
            summary = "Crear permiso",
            tags = {"permission"},
            responses = {
                    @ApiResponse(description = "Éxito", responseCode = "201", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Validación fallida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerToken")
    )
    public ResponseEntity<ResponseBody<PermissionResponseDto>> createPermission(
            @RequestBody PermissionResponseDto permissionDto) {
        try {
            PermissionResponseDto created = permissionService.createPermission(permissionDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiUtil.buildResponseWithDefaults(created));

        } catch (OperationException e) {
            log.warn("Error al crear permiso: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al crear permiso", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Actualizar permiso",
            tags = {"permission"},
            responses = {
                    @ApiResponse(description = "Éxito", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Validación fallida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerToken")
    )
    public ResponseEntity<ResponseBody<PermissionResponseDto>> updatePermission(
            @PathVariable Long id,
            @RequestBody PermissionResponseDto permissionDto) {
        try {
            PermissionResponseDto updated = permissionService.updatePermission(id, permissionDto);
            return ok(ApiUtil.buildResponseWithDefaults(updated));

        } catch (OperationException e) {
            log.warn("Error al actualizar permiso: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al actualizar permiso", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar permiso",
            tags = {"permission"},
            responses = {
                    @ApiResponse(description = "Éxito", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerToken")
    )
    public ResponseEntity<ResponseBody<Boolean>> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ok(ApiUtil.buildResponseWithDefaults(true));

        } catch (OperationException e) {
            log.warn("Error al eliminar permiso: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al eliminar permiso", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/page")
    @Operation(
            summary = "Listar permisos paginado",
            tags = {"permission"},
            responses = {
                    @ApiResponse(description = "Éxito", responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerToken")
    )
    public ResponseEntity<ResponseBody<Page<PermissionResponseDto>>> listPermissions(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "DESC") Sort.Direction sortDir) {
        try {
            Pageable pageable = ApiUtil.buildPageableWithSort(page, size, sortBy, sortDir);
            Page<PermissionResponseDto> permissions = permissionService.findAllPermissions(pageable);
            return ok(ApiUtil.buildResponseWithDefaults(permissions));

        } catch (OperationException e) {
            log.warn("Error al listar permisos: {}", e.getMessage());
            throw ApiResponseException.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al listar permisos", e);
            throw ApiResponseException.serverError(ApiConstants.INTERNAL_SERVER_ERROR);
        }
    }
}