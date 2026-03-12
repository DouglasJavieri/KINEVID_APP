package com.kinevid.kinevidapp.config.security;

import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.model.entity.p.Permission;
import com.kinevid.kinevidapp.rest.model.entity.role.Role;
import com.kinevid.kinevidapp.rest.repository.u.UserRepository;
import com.kinevid.kinevidapp.rest.service.rp.RolePermissionService;
import com.kinevid.kinevidapp.rest.service.u.UserService;
import com.kinevid.kinevidapp.rest.service.ur.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 15/02/2026
 */

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RolePermissionService  rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsernameAuthentication(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username no encontrado" +  username));

        // Mapeamos Roles y Permisos desde tus tablas intermedias
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        List<Role> roles = userRoleService.findRolesByUserId(user.getId());

        for(Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            List<Permission> permissions = rolePermissionService.findPermissionsByRoleId(role.getId());
            for(Permission permission : permissions){
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
