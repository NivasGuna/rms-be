package com.arigs.rms.service;

import com.arigs.rms.dto.request.LoginRequest;
import com.arigs.rms.dto.request.RefreshTokenRequest;
import com.arigs.rms.dto.response.AuthResponse;

/**
 * Authentication use cases.
 */
public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}
