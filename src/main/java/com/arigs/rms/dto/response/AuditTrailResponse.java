package com.arigs.rms.dto.response;

import com.arigs.rms.audit.AuditEventType;
import java.time.Instant;
import java.util.UUID;

/**
 * Audit trail response.
 */
public record AuditTrailResponse(
        UUID id,
        AuditEventType eventType,
        String actor,
        String action,
        String entityType,
        String entityId,
        String details,
        Instant eventDate
) {
}
