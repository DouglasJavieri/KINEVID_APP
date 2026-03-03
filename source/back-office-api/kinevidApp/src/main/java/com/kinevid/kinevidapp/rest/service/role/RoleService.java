package com.kinevid.kinevidapp.rest.service.role;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.role.RoleResponseDto;
import com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 02/03/2026
 */
public interface RoleService {

    RoleResponseDto createRole(RoleResponseDto  role) throws OperationException;
    RoleResponseDto updateRole(Long idRol, RoleResponseDto  role) throws OperationException;
    void deleteRole(Long idRol) throws OperationException;
    Page<RoleResponseDto> findAllRoles(String status, Pageable pageable) throws OperationException;
}
