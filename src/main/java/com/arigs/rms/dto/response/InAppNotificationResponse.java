package com.arigs.rms.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * In-app notification response.
 */
public record InAppNotificationResponse(
        UUID id,
        String title,
        String message,
        String notificationType,
        String entityType,
        String entityId,
        boolean read,
        Instant readAt,
        Instant createdDate
) {
}
