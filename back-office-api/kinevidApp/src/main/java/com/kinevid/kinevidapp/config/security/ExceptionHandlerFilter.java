package com.kinevid.kinevidapp.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinevid.kinevidapp.rest.response.ResponseBody;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 27/03/2026
 * Filtro para capturar excepciones de autenticación JWT
 * Se ejecuta ANTES del JwtAuthenticationFilter
 */
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Excepción capturada en ExceptionHandlerFilter: ", e);
            handleException(response, e);
        }
    }

    /**
     * Maneja las excepciones y retorna respuesta JSON
     */
    private void handleException(HttpServletResponse response, Exception exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ResponseBody<Object> responseBody;

        if (exception instanceof com.kinevid.kinevidapp.config.security.exception.JwtTokenExpiredException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            responseBody = ResponseBody.builder()
                    .code("401")
                    .message("Token expirado. Por favor, use el Refresh Token para obtener uno nuevo.")
                    .data(null)
                    .build();
            log.warn("Token JWT expirado: {}", exception.getMessage());

        } else if (exception instanceof com.kinevid.kinevidapp.config.security.exception.JwtTokenInvalidException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            responseBody = ResponseBody.builder()
                    .code("401")
                    .message("Token inválido o malformado.")
                    .data(null)
                    .build();
            log.warn("Token JWT inválido: {}", exception.getMessage());

        } else if (exception instanceof org.springframework.security.core.AuthenticationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            responseBody = ResponseBody.builder()
                    .code("401")
                    .message("Autenticación fallida: " + exception.getMessage())
                    .data(null)
                    .build();
            log.warn("Error de autenticación: {}", exception.getMessage());

        } else if (exception instanceof org.springframework.security.access.AccessDeniedException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            responseBody = ResponseBody.builder()
                    .code("403")
                    .message("Acceso denegado. No tiene permisos para acceder a este recurso.")
                    .data(null)
                    .build();
            log.warn("Acceso denegado: {}", exception.getMessage());

        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            responseBody = ResponseBody.builder()
                    .code("500")
                    .message("Error interno del servidor. Por favor, contacte al administrador.")
                    .data(null)
                    .build();
            log.error("Error interno no manejado en filtro: ", exception);
        }

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}