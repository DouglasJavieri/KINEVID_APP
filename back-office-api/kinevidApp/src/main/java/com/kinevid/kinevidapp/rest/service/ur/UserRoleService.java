package com.kinevid.kinevidapp.rest.service.ur;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
public interface UserRoleService {

    List<Role> findRolesByUserId(Long idUser) throws OperationException;
    Optional<String> getPrimaryRoleNameByUserId(Long userId) throws OperationException;

}
