package com.kinevid.kinevidapp.rest.service.rp.impl;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.rp.RolePermissionResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.model.entity.rp.RolePermission;
import com.kinevid.kinevidapp.rest.repository.p.PermissionRepository;
import com.kinevid.kinevidapp.rest.repository.role.RoleRepository;
import com.kinevid.kinevidapp.rest.repository.rp.RolePermissionRepository;
import com.kinevid.kinevidapp.rest.service.rp.RolePermissionService;
import com.kinevid.kinevidapp.rest.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findPermissionsByRoleId(Long idRole) {
        try {
            return rolePermissionRepository.findPermissionsByRoleId(idRole);
        } catch (Exception ex) {
            log.error("Error al buscar permisos por rol: {}", idRole, ex);
            throw new RuntimeException("Error al buscar permisos por rol", ex);
        }
    }

    @Override
    @Transactional
    public RolePermissionResponseDto assignPermissionToRole(RolePermissionRequestDto request) throws OperationException {
        try {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new OperationException(FormatUtil.noRegistrado("Rol", request.getRoleId())));

            Permission permission = permissionRepository.findById(request.getPermissionId())
                    .orElseThrow(() -> new OperationException(FormatUtil.noRegistrado("Permiso", request.getPermissionId())));

            if (rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId())) {
                throw new OperationException("El permiso ya está asignado al rol");
            }

            RolePermission rolePermission = RolePermission.builder()
                    .role(role)
                    .permission(permission)
                    .build();

            rolePermissionRepository.save(rolePermission);
            log.info("Permiso {} asignado a rol {}", permission.getName(), role.getName());

            return new RolePermissionResponseDto(rolePermission);

        } catch (OperationException e) {
            log.error("Error al asignar permiso a rol: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al asignar permiso a rol", e);
            throw new OperationException("Error al asignar permiso a rol");
        }
    }

    @Override
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) throws OperationException {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new OperationException(FormatUtil.noRegistrado("Rol", roleId)));

            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new OperationException(FormatUtil.noRegistrado("Permiso", permissionId)));

            RolePermission rolePermission = rolePermissionRepository
                    .findByRoleIdAndPermissionId(role.getId(), permission.getId())
                    .orElseThrow(() -> new OperationException("El permiso no está asignado a este rol"));

            rolePermission.setDeleted(true);
            rolePermissionRepository.save(rolePermission);

            log.info("Permiso {} removido de rol {}", permission.getName(), role.getName());

        } catch (OperationException e) {
            log.error("Error al remover permiso de rol: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al remover permiso de rol", e);
            throw new OperationException("Error al remover permiso de rol");
        }
    }
}