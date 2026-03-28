package com.kinevid.kinevidapp.rest.repository.p;

import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 03/03/2026
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Permission p " +
            "WHERE p.deleted = false " +
            "AND p.name = :name")
    boolean existsPermissionByName(@Param("name") String name);

    @Query("SELECT p FROM Permission p WHERE p.deleted = false AND p.name = :name")
    Optional<Permission> findByName(@Param("name") String name);

}
