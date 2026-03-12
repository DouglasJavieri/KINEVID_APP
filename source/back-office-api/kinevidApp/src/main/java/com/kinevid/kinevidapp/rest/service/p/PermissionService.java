package com.kinevid.kinevidapp.rest.service.p;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.p.PermissionResponseDto;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
public interface PermissionService {

    PermissionResponseDto createPermission(PermissionResponseDto permissionDto) throws OperationException;
    PermissionResponseDto updatePermission(Long idPermission, PermissionResponseDto permissionDto) throws OperationException;
    void deletePermission(Long idPermission) throws OperationException;
}
