package com.kinevid.kinevidapp.rest.repository.u;

import com.kinevid.kinevidapp.rest.model.dto.u.EmailResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.u.UsernameResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE LOWER(u.username) = :username AND u.deleted = false ")
    Optional<User> findByUsernameAuthentication(@Param("username") String username);

    @Query("SELECT new com.kinevid.kinevidapp.rest.model.dto.u.EmailResponseDto(u.email) " +
            "FROM User u " +
            "WHERE u.deleted = false " +
            "AND u.status <> com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus.ELIMINATION " +
            "AND u.email = :email ")
    Optional<EmailResponseDto> findByEmail (@Param("email") String email);

    @Query("SELECT new com.kinevid.kinevidapp.rest.model.dto.u.UsernameResponseDto(u.username) " +
            "FROM User u " +
            "WHERE u.deleted = false " +
            "AND u.status <> com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus.ELIMINATION " +
            "AND u.username = :username ")
    Optional<UsernameResponseDto> findByUsername(@Param("username") String username);

    /**
     * Busca username duplicado excluyendo al propio usuario (para operaciones de actualización)
     */
    @Query("SELECT new com.kinevid.kinevidapp.rest.model.dto.u.UsernameResponseDto(u.username) " +
            "FROM User u " +
            "WHERE u.deleted = false " +
            "AND u.status <> com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus.ELIMINATION " +
            "AND u.username = :username " +
            "AND u.id <> :excludeId")
    Optional<UsernameResponseDto> findByUsernameExcludingId(@Param("username") String username, @Param("excludeId") Long excludeId);

    /**
     * Busca email duplicado excluyendo al propio usuario (para operaciones de actualización)
     */
    @Query("SELECT new com.kinevid.kinevidapp.rest.model.dto.u.EmailResponseDto(u.email) " +
            "FROM User u " +
            "WHERE u.deleted = false " +
            "AND u.status <> com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus.ELIMINATION " +
            "AND u.email = :email " +
            "AND u.id <> :excludeId")
    Optional<EmailResponseDto> findByEmailExcludingId(@Param("email") String email, @Param("excludeId") Long excludeId);

    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u " +
            "WHERE u.deleted = false " +
            "AND u.status <> com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus.ELIMINATION")
    Page<User> findAllActive(Pageable pageable);

}
