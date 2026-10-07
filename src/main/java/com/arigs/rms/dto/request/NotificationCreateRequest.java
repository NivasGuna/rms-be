package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

/**
 * Request to create in-app notifications.
 */
public record NotificationCreateRequest(
        @NotEmpty Set<UUID> recipientUserIds,
        @NotBlank @Size(max = 160) String title,
        @NotBlank String message,
        @NotBlank @Size(max = 60) String notificationType,
        @Size(max = 80) String entityType,
        @Size(max = 80) String entityId
) {
}
