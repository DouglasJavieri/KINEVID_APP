package com.kinevid.kinevidapp.rest.service.u;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.u.EmailResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.u.UserRequestDTO;
import com.kinevid.kinevidapp.rest.model.dto.u.UserResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */
public interface UserService {

    Optional<User> findByUsernameAuthentication(String username);
    boolean existsUsername(String username) throws OperationException;
    boolean existsEmail(String email) throws OperationException;

    UserResponseDto createUser(UserRequestDTO user) throws OperationException;
    UserResponseDto updateUser(Long id, UserRequestDTO user) throws OperationException;
    void deleteUser(Long id) throws OperationException;

}
