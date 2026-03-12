package com.kinevid.kinevidapp.rest.service.rp;

import com.kinevid.kinevidapp.rest.model.entity.p.Permission;

import java.util.List;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
public interface RolePermissionService {

    List<Permission> findPermissionsByRoleId(Long idRole);
}
