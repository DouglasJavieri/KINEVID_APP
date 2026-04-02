
export interface LoginRequest {
  username: string;
  password: string;
}

/** POST /api/auth/refresh */
export interface RefreshTokenRequest {
  refreshToken: string;
}

/** POST /api/auth/logout */
export interface LogoutRequest {
  refreshToken: string;
}

// ─── RESPONSE ─────────────────────────────────────────────────────────────────

/** Información del usuario autenticado (dentro de JwtResponse) */
export interface UserAuthInfo {
  id: number;
  username: string;
  email: string;
  fullName: string | null;
  role: string | null;
}

/** Respuesta de login y refresh — contiene ambos tokens */
export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  userInfo: UserAuthInfo;
}

/** Envoltura genérica que usa el backend en todas las respuestas */
export interface ApiResponse<T> {
  code: string;
  message: string;
  data: T;
}


/**
 * Enum de roles del sistema.
 * Los valores coinciden exactamente con los roles definidos en el backend
 * (Spring Security convention: prefijo ROLE_).
 *
 * Usar siempre este enum en guards, directivas y rutas.
 * NUNCA hardcodear strings de roles directamente en el código.
 *
 * @see SecurityConfig.java — kinevid.app.admin.role=ROLE_ADMIN
 */
export enum AppRole {
  ADMIN          = 'ROLE_ADMIN',
  // Añadir aquí los nuevos roles cuando el backend los defina:
  // FISIOTERAPEUTA = 'ROLE_FISIOTERAPEUTA',
  // RECEPCIONISTA  = 'ROLE_RECEPCIONISTA',
}
