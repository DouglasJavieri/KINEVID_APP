package com.kinevid.kinevidapp.rest.service.u.impl;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.u.EmailResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.u.UserRequestDTO;
import com.kinevid.kinevidapp.rest.model.dto.u.UserResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.u.UsernameResponseDto;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import com.kinevid.kinevidapp.rest.repository.u.UserRepository;
import com.kinevid.kinevidapp.rest.service.u.UserService;
import com.kinevid.kinevidapp.rest.util.FormatUtil;
import com.kinevid.kinevidapp.rest.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameAuthentication(String username) {
        try {
            return userRepository.findByUsernameAuthentication(username);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar usuario por username: " + username, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUsername(String username) {
        try {
            Optional<UsernameResponseDto> user = this.userRepository.findByUsername(username);
            return user.isPresent();
        } catch (Exception ex) {
            throw new RuntimeException("Error al verificar existencia por username: " + username, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsEmail(String email) {
        try {
            String cleanEmail = email.trim();
            cleanEmail = cleanEmail.replace(" ", "+");
            cleanEmail = cleanEmail.toLowerCase();
            Optional<EmailResponseDto> emailModel = this.userRepository.findByEmail(cleanEmail);
            return emailModel.isPresent();
        } catch (Exception ex) {
            throw new RuntimeException("Error al verificar existencia por email: " + email, ex);
        }
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDTO user) throws OperationException {
        try {
            ValidationUtil.throwExceptionIfInvalidText("Usuario", user.getUsername(), true, 30);
            ValidationUtil.throwExceptionIfInvalidText("Email", user.getEmail(), true, 50);
            ValidationUtil.throwExceptionIfInvalidText("Password", user.getPassword(), true, 100);

            if((user.getEmail() == null || user.getEmail().isBlank()) && (user.getPassword() == null || user.getPassword().isBlank())) {
                log.info("Campos email y contraseña vacíos");
                throw new OperationException("Campos email y contraseña vacíos");
            }

            Optional<UsernameResponseDto> findByUsername = userRepository.findByUsername(user.getUsername());
            if(findByUsername.isPresent()) {
                throw new OperationException(FormatUtil.yaRegistrado("Usuario", "Usuario", user.getUsername()));
            }
            Optional<EmailResponseDto> findByEmail = userRepository.findByEmail(user.getEmail());
            if(findByEmail.isPresent()) {
                throw new OperationException(FormatUtil.yaRegistrado("Email", "Email", user.getEmail()));
            }

            User userModel = User.builder()
                    .username(user.getUsername().toLowerCase().trim())
                    .email(user.getEmail().toLowerCase().trim())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(userModel);

            return new UserResponseDto(userModel);
        } catch (OperationException e) {
            log.error("Error de operación al crear al usuario {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al crear usuario", e);
            throw new OperationException("Ocurrió un error inesperado al crear al usuario");
        }
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDTO user) throws OperationException {
        try {
            ValidationUtil.throwExceptionIfInvalidText("Usuario", user.getUsername(), true, 30);
            ValidationUtil.throwExceptionIfInvalidText("Email", user.getEmail(), true, 50);
            ValidationUtil.throwExceptionIfInvalidText("Password", user.getPassword(), true, 100);

            User userModel = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Optional<UsernameResponseDto> findByUsername = userRepository.findByUsername(user.getUsername());
            if(findByUsername.isPresent()) {
                throw new OperationException(FormatUtil.yaRegistrado("Usuario", "Usuario", user.getUsername()));
            }
            Optional<EmailResponseDto> findByEmail = userRepository.findByEmail(user.getEmail());
            if(findByEmail.isPresent()) {
                throw new OperationException(FormatUtil.yaRegistrado("Email", "Email", user.getEmail()));
            }

            userModel.setUsername(user.getUsername().toLowerCase().trim());
            userModel.setEmail(user.getEmail().toLowerCase().trim());

            if(user.getPassword() != null && !user.getPassword().isBlank()) {
                userModel.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(userModel);
            return new UserResponseDto(userModel);
        } catch (OperationException e) {
            log.error("Error de operación al actualizar al usuario {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error generico al actualizar usuario", e);
            throw new OperationException("Ocurrió un error inesperado al actualizar al usuario");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws OperationException {
        try {
            User userModel = userRepository.findById(id)
                    .orElseThrow(() -> new OperationException(FormatUtil.noRegistrado("Usuario", id)));
            userModel.setDeleted(true);
            userModel.setStatus(UserStatus.ELIMINATION);
            userRepository.save(userModel);
        } catch (OperationException e) {
            log.error("Error de operación al eliminar rol {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error inesperado al eliminar rol {}", e.getMessage(), e);
            throw new OperationException("Ocurrió un error inesperado al eliminar el rol.");
        }
    }
}
