package com.kinevid.kinevidapp.rest.service.p.impl;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.p.PermissionResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import com.kinevid.kinevidapp.rest.repository.p.PermissionRepository;
import com.kinevid.kinevidapp.rest.service.p.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional
    public PermissionResponseDto createPermission(PermissionResponseDto permissionDto) throws OperationException {
        try {
            if(permissionRepository.existsPermissionByName(permissionDto.getName())){
                throw new OperationException("Ya existe un permiso con el nombre: " +  permissionDto.getName());
            }

            Permission permissionModel = Permission.builder()
                    .name(permissionDto.getName().toUpperCase().strip())
                    .description(permissionDto.getDescription().strip())
                    .build();
            permissionRepository.save(permissionModel);
            return new PermissionResponseDto(permissionModel);
        } catch (OperationException e) {
            log.error("Error de operación al crear un permiso {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al crear un permiso", e);
            throw new OperationException("Ocurrió un error inesperado al crear un permiso");
        }
    }

    @Override
    @Transactional
    public PermissionResponseDto updatePermission(Long idPermission, PermissionResponseDto permissionDto) throws OperationException {
        try {
            Permission permissionModel = permissionRepository.findById(idPermission)
                    .orElseThrow(() -> new OperationException("No existe el permiso con el id: " +  idPermission));

            if(permissionRepository.existsPermissionByName(permissionDto.getName())){
                throw new OperationException("Ya existe un permiso con el nombre: " +  permissionDto.getName());
            }
            permissionModel.setName(permissionDto.getName().toUpperCase().strip());
            permissionModel.setDescription(permissionDto.getDescription().strip());
            permissionRepository.save(permissionModel);
            return new PermissionResponseDto(permissionModel);
        } catch (OperationException e) {
            log.error("Error de operación al actualizar un permiso {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al actualizar un permiso", e);
            throw new OperationException("Ocurrió un error inesperado al actualizar un permiso");
        }
    }

    @Override
    @Transactional
    public void deletePermission(Long idPermission) throws OperationException {
        try {
            Permission permissionModel = permissionRepository.findById(idPermission)
                    .orElseThrow(() -> new OperationException("No existe el permiso con el id: " +  idPermission));
            permissionModel.setDeleted(true);
            permissionRepository.save(permissionModel);
        } catch (OperationException e) {
            log.error("Error de operación al eliminar rol {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al eliminar rol {}", e.getMessage(), e);
            throw new OperationException("Ocurrió un error inesperado al eliminar el rol.");
        }
    }
}
