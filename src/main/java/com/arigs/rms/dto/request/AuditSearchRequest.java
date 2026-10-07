package com.arigs.rms.dto.request;

import com.arigs.rms.audit.AuditEventType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;

/**
 * Audit trail search request.
 */
public record AuditSearchRequest(
        AuditEventType eventType,
        String actor,
        String action,
        String entityType,
        String entityId,
        Instant from,
        Instant to,
        @Min(0) Integer page,
        @Min(1) @Max(200) Integer size
) {
    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 50 : size;
    }
}
