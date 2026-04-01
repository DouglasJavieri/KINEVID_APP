package com.kinevid.kinevidapp.rest.service.u;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.u.UserRequestDTO;
import com.kinevid.kinevidapp.rest.model.dto.u.UserResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.u.UserUpdateRequestDTO;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */
public interface UserService {

    // --- Autenticación (uso interno de Spring Security) ---
    Optional<User> findByUsernameAuthentication(String username);
    boolean existsUsername(String username) throws OperationException;
    boolean existsEmail(String email) throws OperationException;
    UserResponseDto createUser(UserRequestDTO user) throws OperationException;
    UserResponseDto getUserById(Long id) throws OperationException;
    Page<UserResponseDto> getAllUsers(Pageable pageable) throws OperationException;
    UserResponseDto updateUser(Long id, UserUpdateRequestDTO user) throws OperationException;
    UserResponseDto changeUserStatus(Long id, UserStatus status) throws OperationException;
    void deleteUser(Long id) throws OperationException;

}
