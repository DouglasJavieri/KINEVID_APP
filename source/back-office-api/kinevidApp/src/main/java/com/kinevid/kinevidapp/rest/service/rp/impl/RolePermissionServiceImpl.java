package com.kinevid.kinevidapp.rest.service.rp.impl;

import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import com.kinevid.kinevidapp.rest.repository.rp.RolePermissionRepository;
import com.kinevid.kinevidapp.rest.service.rp.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
@Service
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findPermissionsByRoleId(Long idRole) {
        try {
            return rolePermissionRepository.findPermissionsByRoleId(idRole);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar permisos por role con id = {}: " + idRole, ex);
        }
    }
}
