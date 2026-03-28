package com.kinevid.kinevidapp.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 * Configuración de seguridad Spring Security
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Bean principal de la cadena de filtros de seguridad
     * Define:
     * 1. Rutas públicas (sin autenticación)
     * 2. Rutas protegidas (requieren autenticación)
     * 3. Orden correcto de filtros JWT
     * 4. Políticas de sesión STATELESS (sin cookies)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Desabilitar CSRF (usamos JWT, no cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // Política de sesión: STATELESS
                // Cada request es independiente, no se mantiene estado en servidor
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Definir autorización por rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas - NO requieren autenticación
                        .requestMatchers(
                                "/api/auth/login",              //  Login (acceso público)
                                "/api/auth/forgot-password",    //  Solicitar recuperación contraseña
                                "/api/auth/reset-password",     //  Validar código y cambiar contraseña
                                "/swagger-ui/**",               //  Swagger UI (documentación)
                                "/v3/api-docs/**",              //  OpenAPI docs
                                "/actuator/health"              //  Health check
                        ).permitAll()

                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )

                // Provider de autenticación (DAO - base de datos)
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean para el proveedor de autenticación DAO
     * Implementación de Spring Security que:
     * - Usa UserDetailsService para cargar usuarios de la BD
     * - Usa PasswordEncoder para validar contraseñas
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Bean para el AuthenticationManager
     * Se inyecta en los endpoints de login (AuthController)
     * para autenticar usuarios usando username + password
     * UsernamePasswordAuthenticationToken authToken =
     *     new UsernamePasswordAuthenticationToken(username, password);
     * Authentication auth = authenticationManager.authenticate(authToken);
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}