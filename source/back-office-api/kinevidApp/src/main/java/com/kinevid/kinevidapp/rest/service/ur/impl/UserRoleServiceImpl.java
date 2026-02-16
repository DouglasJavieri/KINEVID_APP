package com.kinevid.kinevidapp.rest.service.ur.impl;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.repository.ur.UserRoleRepository;
import com.kinevid.kinevidapp.rest.service.ur.UserRoleService;
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
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> findRolesByUserId(Long idUser) {
        try {
            return userRoleRepository.findRolesByUserId(idUser);
        } catch (Exception e) {
            log.info("Error al buscar roles para el usuario con ID: {} ", idUser, e);
            throw new OperationException("Error al buscar roles para el usuario", e.getCause());
        }
    }
}
