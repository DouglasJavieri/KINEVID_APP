package com.kinevid.kinevidapp.rest.service.rp;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.p.Permission;

import java.util.List;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
public interface RolePermissionService {

    List<Permission> findPermissionsByRoleId(Long idRole) throws OperationException;
    RolePermissionResponseDto assignPermissionToRole(RolePermissionRequestDto request) throws OperationException;
    void removePermissionFromRole(Long roleId, Long permissionId) throws OperationException;

}
