package com.kinevid.kinevidapp.rest.service.u;

import com.kinevid.kinevidapp.rest.model.entity.auth.User;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */
public interface UserService {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
