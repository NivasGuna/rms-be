package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Login credentials.
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
