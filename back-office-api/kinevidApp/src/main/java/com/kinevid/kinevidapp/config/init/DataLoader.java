package com.kinevid.kinevidapp.config.init;

import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.model.entity.rp.RolePermission;
import com.kinevid.kinevidapp.rest.model.entity.ur.UserRole;
import com.kinevid.kinevidapp.rest.model.enums.auth.UserStatus;
import com.kinevid.kinevidapp.rest.model.enums.role.RoleStatus;
import com.kinevid.kinevidapp.rest.repository.p.PermissionRepository;
import com.kinevid.kinevidapp.rest.repository.role.RoleRepository;
import com.kinevid.kinevidapp.rest.repository.rp.RolePermissionRepository;
import com.kinevid.kinevidapp.rest.repository.u.UserRepository;
import com.kinevid.kinevidapp.rest.repository.ur.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "douvan.22@gmail.com";
    private static final String ADMIN_PASSWORD = "Abc123**";
    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Iniciando DataLoader");

        try {
            List<Permission> permissions = createDefaultPermissions();
            Role adminRole = createAdminRole(permissions);
            User adminUser = createAdminUser();
            assignAdminRoleToUser(adminUser, adminRole);

            log.info("DataLoader completado exitosamente");

        } catch (Exception e) {
            log.error("Error en DataLoader", e);
            throw new RuntimeException("Error al cargar datos iniciales", e);
        }
    }

    private List<Permission> createDefaultPermissions() {
        log.info("Creando permisos por defecto");

        String[][] permissionsData = {
                {"CREATE_USER", "Crear usuario"},
                {"VIEW_USER", "Ver usuario"},
                {"UPDATE_USER", "Actualizar usuario"},
                {"DELETE_USER", "Eliminar usuario"},
                {"LIST_USER", "Listar usuarios"},
                {"CHANGE_USER_STATUS", "Cambiar estado usuario"},
                {"CREATE_ROLE", "Crear rol"},
                {"READ_ROLE", "Ver rol"},
                {"UPDATE_ROLE", "Actualizar rol"},
                {"DELETE_ROLE", "Eliminar rol"},
                {"LIST_ROLE", "Listar roles"},
                {"CREATE_PERMISSION", "Crear permiso"},
                {"READ_PERMISSION", "Ver permiso"},
                {"UPDATE_PERMISSION", "Actualizar permiso"},
                {"DELETE_PERMISSION", "Eliminar permiso"},
                {"LIST_PERMISSION", "Listar permisos"},
                {"ASSIGN_PERMISSION_TO_ROLE", "Asignar permisos a rol"},
                {"REMOVE_PERMISSION_FROM_ROLE", "Remover permisos de rol"},
        };

        for (String[] permData : permissionsData) {
            if (!permissionRepository.existsPermissionByName(permData[0])) {
                Permission permission = Permission.builder()
                        .name(permData[0])
                        .description(permData[1])
                        .build();
                permissionRepository.save(permission);
                log.debug("Permiso creado: {}", permData[0]);
            }
        }

        return permissionRepository.findAll();
    }

    private Role createAdminRole(List<Permission> allPermissions) {
        log.info("Creando rol ADMIN");

        Role adminRole = roleRepository.findByName(ADMIN_ROLE_NAME)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(ADMIN_ROLE_NAME)
                            .description("Rol de administrador con acceso total")
                            .status(RoleStatus.ACTIVE)
                            .build();

                    Role savedRole = roleRepository.save(newRole);

                    for (Permission permission : allPermissions) {
                        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(
                                savedRole.getId(), permission.getId())) {
                            RolePermission rolePermission = RolePermission.builder()
                                    .role(savedRole)
                                    .permission(permission)
                                    .build();
                            rolePermissionRepository.save(rolePermission);
                        }
                    }

                    log.info("Rol ADMIN creado con {} permisos", allPermissions.size());
                    return savedRole;
                });

        return adminRole;
    }

    private User createAdminUser() {
        log.info("Creando usuario admin");

        User adminUser = userRepository.findByUsernameAuthentication(ADMIN_USERNAME)
                .orElseGet(() -> {
                    String hashedPassword = passwordEncoder.encode(ADMIN_PASSWORD);

                    User newUser = User.builder()
                            .username(ADMIN_USERNAME)
                            .email(ADMIN_EMAIL)
                            .password(hashedPassword)
                            .status(UserStatus.ACTIVE)
                            .build();

                    User savedUser = userRepository.save(newUser);
                    log.warn("Usuario admin creado - Contraseña: {}", ADMIN_PASSWORD);
                    return savedUser;
                });

        return adminUser;
    }

    private void assignAdminRoleToUser(User adminUser, Role adminRole) {
        log.info("Asignando rol ADMIN al usuario admin");

        if (!userRoleRepository.existsByUserIdAndRoleId(adminUser.getId(), adminRole.getId())) {
            UserRole userRole = UserRole.builder()
                    .user(adminUser)
                    .role(adminRole)
                    .build();
            userRoleRepository.save(userRole);
            log.info("Rol ADMIN asignado");
        }
    }
}