package com.kinevid.kinevidapp.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinevid.kinevidapp.config.security.exception.JwtTokenExpiredException;
import com.kinevid.kinevidapp.config.security.exception.JwtTokenInvalidException;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 16/02/2026
 * Filtro JWT para autenticar usuarios basándose en Access Token
 * Se ejecuta DESPUÉS de ExceptionHandlerFilter
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Paso 1: Extraer el token del header Authorization
            String authHeader = extractAuthHeader(request);

            if (authHeader == null) {
                // Sin token, continuar sin autenticar (puede ser endpoint público)
                log.debug("Sin token en request: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractToken(authHeader);

            // Paso 2: Validar el Access Token
            if (!jwtUtils.validateAccessToken(token)) {
                log.warn("Access Token inválido o expirado en request: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            // Paso 3: Extraer username del token
            String username = jwtUtils.getUsernameFromAccessToken(token);
            log.debug("Token válido para usuario: {}", username);

            // Paso 4: Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Paso 5: Crear autenticación y establecerla en el contexto
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Usuario autenticado: {} en endpoint: {}", username, request.getRequestURI());

        } catch (JwtTokenExpiredException e) {
            // Token expirado - Retornar 401
            log.warn("Access Token expirado: {}", e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "401",
                    "Access Token expirado. Use el Refresh Token para obtener uno nuevo.");
            return;

        } catch (JwtTokenInvalidException e) {
            // Token inválido - Retornar 401
            log.warn("Access Token inválido: {}", e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "401",
                    "Access Token inválido o malformado.");
            return;

        } catch (UsernameNotFoundException e) {
            // Usuario no encontrado - Retornar 401
            log.warn("Usuario no encontrado: {}", e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "401",
                    "Usuario no encontrado.");
            return;

        } catch (Exception e) {
            // Error genérico - Retornar 500
            log.error("Error inesperado en JwtAuthenticationFilter: ", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500",
                    "Error interno del servidor.");
            return;
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el header Authorization del request
     * @param request HttpServletRequest
     * @return Header de Autorización o null
     */
    private String extractAuthHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header;
        }
        return null;
    }

    /**
     * Extrae el token del header Authorization
     * Formato esperado: "Bearer <token>"
     * @param authHeader Header Authorization
     * @return Token sin el prefijo "Bearer "
     */
    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    /**
     * Envía respuesta de error en formato JSON
     * @param response HttpServletResponse
     * @param status Código HTTP (401, 403, 500, etc)
     * @param code Código de error personalizado
     * @param message Mensaje de error
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ResponseBody<Object> responseBody = ResponseBody.builder()
                .code(code)
                .message(message)
                .data(null)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        log.debug("Respuesta de error enviada: {} - {}", status, message);
    }

    /**
     * Opcional: Método para extraer el token desde el contexto de seguridad
     * Útil para controllers que necesitan el token actual
     * @return Token actual o null
     */
    public static String getCurrentToken() {
        // Este método puede usarse en controllers para obtener el token actual
        return null; // Implementar si se necesita
    }
}