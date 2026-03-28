package com.kinevid.kinevidapp.rest.service.auth.impl;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.entity.auth.RefreshToken;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;
import com.kinevid.kinevidapp.rest.repository.auth.RefreshTokenRepository;
import com.kinevid.kinevidapp.rest.service.auth.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Douglas Cristhian Javieri Vino
 * @created 27/03/2026
 * Implementación del servicio RefreshTokenService
 */
@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${kinevid.app.jwtRefreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user, String ipAddress, String userAgent) throws OperationException {
        try {
            if (user == null || user.getId() == null) {
                throw new OperationException("Usuario inválido para crear Refresh Token");
            }

            // Calcular fecha de expiración
            LocalDateTime expiryDate = LocalDateTime.now()
                    .plusNanos(refreshTokenExpirationMs * 1_000_000); // Convertir ms a ns

            // Crear token (sin el JWT real, ese se genera en JwtUtils)
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .revoked(false)
                    .expiryDate(expiryDate)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();

            refreshToken = refreshTokenRepository.save(refreshToken);
            log.info("Refresh Token creado para usuario: {}", user.getUsername());
            return refreshToken;

        } catch (OperationException e) {
            log.error("Error de operación al crear Refresh Token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear Refresh Token", e);
            throw new OperationException("Error al crear Refresh Token", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) throws OperationException {
        try {
            if (token == null || token.isBlank()) {
                throw new OperationException("Token inválido");
            }
            return refreshTokenRepository.findByToken(token);
        } catch (OperationException e) {
            log.error("Error de operación al buscar Refresh Token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al buscar Refresh Token", e);
            throw new OperationException("Error al buscar Refresh Token", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateRefreshToken(String token) throws OperationException {
        try {
            if (token == null || token.isBlank()) {
                return false;
            }

            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

            if (refreshToken.isEmpty()) {
                log.warn("Refresh Token no encontrado");
                return false;
            }

            RefreshToken rt = refreshToken.get();

            // Verificar si está revocado
            if (rt.getRevoked()) {
                log.warn("Refresh Token ha sido revocado para usuario: {}", rt.getUser().getUsername());
                return false;
            }

            // Verificar si ha expirado
            if (rt.isExpired()) {
                log.warn("Refresh Token expirado para usuario: {}", rt.getUser().getUsername());
                return false;
            }

            return true;
        } catch (OperationException e) {
            log.error("Error de operación al validar Refresh Token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al validar Refresh Token", e);
            throw new OperationException("Error al validar Refresh Token", e);
        }
    }


    @Override
    @Transactional
    public void revokeToken(String token) throws OperationException {
        try {
            if (token == null || token.isBlank()) {
                throw new OperationException("Token inválido");
            }

            int affectedRows = refreshTokenRepository.revokeToken(token);

            if (affectedRows == 0) {
                log.warn("No se encontró Refresh Token para revocar: {}", token);
            } else {
                log.info("Refresh Token revocado: {} filas afectadas", affectedRows);
            }
        } catch (OperationException e) {
            log.error("Error de operación al revocar Refresh Token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al revocar Refresh Token", e);
            throw new OperationException("Error al revocar Refresh Token", e);
        }
    }


    @Override
    @Transactional
    public void revokeAllUserTokens(Long userId) throws OperationException {
        try {
            if (userId == null || userId <= 0) {
                throw new OperationException("ID de usuario inválido");
            }

            int affectedRows = refreshTokenRepository.revokeAllUserTokens(userId);
            log.info("Todos los Refresh Tokens revocados para usuario ID: {} - Filas afectadas: {}", userId, affectedRows);

        } catch (OperationException e) {
            log.error("Error de operación al revocar tokens del usuario: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al revocar tokens del usuario", e);
            throw new OperationException("Error al revocar tokens del usuario", e);
        }
    }


    @Override
    @Transactional
    public void deleteExpiredTokens() throws OperationException {
        try {
            LocalDateTime now = LocalDateTime.now();
            int affectedRows = refreshTokenRepository.deleteExpiredTokens(now);
            log.info("Tokens expirados eliminados: {} filas afectadas", affectedRows);

        } catch (Exception e) {
            log.error("Error al eliminar tokens expirados", e);
            throw new OperationException("Error al eliminar tokens expirados", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countValidTokensForUser(Long userId) throws OperationException {
        try {
            if (userId == null || userId <= 0) {
                throw new OperationException("ID de usuario inválido");
            }
            return refreshTokenRepository.countValidTokensByUserId(userId);
        } catch (OperationException e) {
            log.error("Error de operación al contar tokens válidos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al contar tokens válidos", e);
            throw new OperationException("Error al contar tokens válidos", e);
        }
    }
}