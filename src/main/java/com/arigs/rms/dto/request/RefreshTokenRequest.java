package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Refresh token exchange request.
 */
public record RefreshTokenRequest(@NotBlank String refreshToken) {
}
