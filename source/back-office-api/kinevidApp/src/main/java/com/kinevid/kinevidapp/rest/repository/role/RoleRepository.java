package com.kinevid.kinevidapp.rest.repository.role;

import com.kinevid.kinevidapp.rest.model.dto.role.RoleResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 02/03/2026
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Role r " +
            "WHERE r.deleted = false AND r.status <> com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus.ELIMINATION " +
            "AND r.name = :name")
    boolean existsRoleByName(@Param("name") String name);

    @Query("SELECT new com.kinevid.kinevidapp.rest.model.dto.role.PagedRoleResponseDto(r) " +
            "FROM Role r " +
            "WHERE r.deleted = false AND (:status IS NULL OR r.status = :status) ")
    Page<RoleResponseDto> findAllRoles(@Param("status") RoleStatus status,
                                       Pageable pageable);
}
