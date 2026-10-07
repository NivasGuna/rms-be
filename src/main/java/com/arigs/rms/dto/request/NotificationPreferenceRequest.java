package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request to update notification preferences.
 */
public record NotificationPreferenceRequest(
        @NotNull UUID userId,
        @NotBlank String notificationType,
        boolean emailEnabled,
        boolean inAppEnabled
) {
}
