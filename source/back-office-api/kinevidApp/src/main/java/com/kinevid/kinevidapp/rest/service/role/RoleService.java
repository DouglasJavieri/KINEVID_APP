package com.kinevid.kinevidapp.rest.service.role;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.role.PagedRoleResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.role.RoleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 02/03/2026
 */
public interface RoleService {

    RoleResponseDto createRole(RoleResponseDto  role) throws OperationException;
    RoleResponseDto updateRole(Long idRol, RoleResponseDto  role) throws OperationException;
    void deleteRole(Long idRol) throws OperationException;
    Page<PagedRoleResponseDto> findAllRoles(String status, Pageable pageable) throws OperationException;
    void changeStatus(Long idRol, String status) throws OperationException;
}
