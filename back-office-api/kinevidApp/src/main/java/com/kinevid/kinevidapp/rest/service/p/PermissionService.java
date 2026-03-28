package com.kinevid.kinevidapp.rest.service.p;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.p.PermissionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
public interface PermissionService {

    PermissionResponseDto createPermission(PermissionResponseDto permissionDto) throws OperationException;
    PermissionResponseDto updatePermission(Long idPermission, PermissionResponseDto permissionDto) throws OperationException;
    void deletePermission(Long idPermission) throws OperationException;
    Page<PermissionResponseDto> findAllPermissions(Pageable pageable) throws OperationException;
    void changeStatus(Long idPermission, String status) throws OperationException;

}
