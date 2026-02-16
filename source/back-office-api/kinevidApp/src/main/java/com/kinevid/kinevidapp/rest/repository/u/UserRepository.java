package com.kinevid.kinevidapp.rest.repository.u;

import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
