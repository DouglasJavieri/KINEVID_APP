package com.kinevid.kinevidapp.rest.repository.ur;

import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.model.entity.ur.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur.role " +
            "FROM UserRole ur " +
            "WHERE ur.user.id = :idUser ")
    List<Role> findRolesByUserId(@Param("idUser") Long idUser);
}
