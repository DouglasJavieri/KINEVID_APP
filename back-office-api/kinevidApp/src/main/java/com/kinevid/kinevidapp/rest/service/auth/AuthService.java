package com.kinevid.kinevidapp.rest.service.auth;

import com.kinevid.kinevidapp.rest.exception.OperationException;
import com.kinevid.kinevidapp.rest.model.dto.auth.ChangePasswordRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.JwtResponseDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.LoginRequestDto;
import com.kinevid.kinevidapp.rest.model.dto.auth.RefreshTokenRequestDto;
import com.kinevid.kinevidapp.rest.model.entity.auth.User;

public interface AuthService {

    JwtResponseDto login(LoginRequestDto loginRequest) throws OperationException;

    JwtResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequest) throws OperationException;

    void logout(String refreshToken) throws OperationException;

    void changePassword(ChangePasswordRequestDto changePasswordRequest) throws OperationException;

    User getCurrentUser() throws OperationException;
}