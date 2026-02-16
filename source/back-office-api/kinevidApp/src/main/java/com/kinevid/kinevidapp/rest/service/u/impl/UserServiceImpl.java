package com.kinevid.kinevidapp.rest.service.u.impl;

import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.repository.u.UserRepository;
import com.kinevid.kinevidapp.rest.service.u.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar usuario por username: " + username, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        try {
            return userRepository.existsByUsername(username);
        } catch (Exception ex) {
            throw new RuntimeException("Error al verificar existencia por username: " + username, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception ex) {
            throw new RuntimeException("Error al verificar existencia por email: " + email, ex);
        }
    }

}
