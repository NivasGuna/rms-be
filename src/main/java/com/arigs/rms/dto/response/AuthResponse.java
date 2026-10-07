package com.arigs.rms.dto.response;

import java.time.Instant;
import java.util.Set;

/**
 * JWT authentication response.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        Instant refreshTokenExpiresAt,
        String tokenType,
        UserResponse user
) {
}
