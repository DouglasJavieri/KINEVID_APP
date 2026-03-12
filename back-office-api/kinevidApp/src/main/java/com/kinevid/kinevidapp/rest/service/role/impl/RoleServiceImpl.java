package com.kinevid.kinevidapp.rest.service.role.impl;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.role.PagedRoleResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.role.RoleResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus;
import com.kinevid.kinevidapp.rest.repository.role.RoleRepository;
import com.kinevid.kinevidapp.rest.service.role.RoleService;
import com.kinevid.kinevidapp.rest.util.FormatUtil;
import com.kinevid.kinevidapp.rest.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 02/03/2026
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PagedRoleResponseDto> findAllRoles(String status, Pageable pageable) throws OperationException {
        try {
            RoleStatus roleStatus = RoleStatus.valueOf(status);
            return roleRepository.findAllRoles(roleStatus, pageable);
        } catch (OperationException e) {
            log.error("Error de operación al crear al rol {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al crear un rol", e);
            throw new OperationException("Ocurrió un error inesperado al crear al rol");
        }
    }

    @Override
    @Transactional
    public RoleResponseDto createRole(RoleResponseDto role) throws OperationException {
        try {
            ValidationUtil.throwExceptionIfInvalidText("Nombre Rol", role.getName(), true, 75);
            ValidationUtil.throwExceptionIfInvalidText("Descripcion", role.getDescription(), true, 255);

            if (roleRepository.existsRoleByName(role.getName())) {
                throw new OperationException("Ya existe un rol con el nombre: " + role.getName());
            }

            Role roleModel = Role.builder()
                    .name(role.getName().toUpperCase().strip())
                    .description(role.getDescription().strip())
                    .status(RoleStatus.ACTIVE)
                    .build();
            roleRepository.save(roleModel);

            return new RoleResponseDto(roleModel);
        } catch (OperationException e) {
            log.error("Error de operación al crear al rol {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al crear un rol", e);
            throw new OperationException("Ocurrió un error inesperado al crear al rol");
        }
    }

    @Override
    @Transactional
    public RoleResponseDto updateRole(Long idRol, RoleResponseDto role) throws OperationException {
        try {
            ValidationUtil.throwExceptionIfInvalidText("Nombre Rol", role.getName(), true, 75);
            ValidationUtil.throwExceptionIfInvalidText("Descripcion", role.getDescription(), true, 255);

            Role roleModel = roleRepository.findById(idRol)
                    .orElseThrow(() -> new OperationException("Rol no encontrado"));

            if (roleRepository.existsRoleByName(role.getName())) {
                throw new OperationException("Ya existe un rol con el nombre: " + role.getName());
            }

            roleModel.setName(role.getName().toUpperCase().strip());
            roleModel.setDescription(role.getDescription().strip());
            roleRepository.save(roleModel);
            return new RoleResponseDto(roleModel);

        } catch (OperationException e) {
            log.error("Error de operación al actualizar al rol {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al actualizar un rol", e);
            throw new OperationException("Ocurrió un error inesperado al actualizar al rol");
        }
    }

    @Override
    @Transactional
    public void deleteRole(Long idRol) throws OperationException {
        try {
            Role roleModel = roleRepository.findById(idRol)
                    .orElseThrow(()-> new OperationException(FormatUtil.noRegistrado("Rol", idRol)));
            roleModel.setDeleted(true);
            roleModel.setStatus(RoleStatus.ELIMINATION);
            roleRepository.save(roleModel);
        } catch (OperationException e) {
            log.error("Error de operación al eliminar rol {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al eliminar rol {}", e.getMessage(), e);
            throw new OperationException("Ocurrió un error inesperado al eliminar el rol.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void changeStatus(Long idRol, String status) throws OperationException {
        try {
            Role role = roleRepository.findById(idRol)
                    .orElseThrow(() -> new OperationException("No existe un rol con id " + idRol));

            RoleStatus roleStatus = RoleStatus.valueOf(status);
            role.setStatus(roleStatus);
            roleRepository.save(role);
        } catch (OperationException e) {
            log.error("Error de operación al cambiar estado de rol {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al cambiar estado de rol {}", e.getMessage(), e);
            throw new OperationException("Ocurrió un error inesperado al cambiar estado de rol.");
        }
    }
}
