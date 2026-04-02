package com.kinevid.kinevidapp.rest.service.rp;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.p.PermissionResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionResponseDto;

import java.util.List;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
public interface RolePermissionService {

    List<PermissionResponseDto> getPermissionsByRoleId(Long roleId) throws OperationException;
    RolePermissionResponseDto assignPermissionToRole(RolePermissionRequestDto request) throws OperationException;
    void removePermissionFromRole(Long roleId, Long permissionId) throws OperationException;

}
