package com.arigs.rms.audit;

/**
 * Writes auditable application events.
 */
public interface AuditService {

    void record(AuditEventType eventType, String action, String entityType, String entityId, String details);
}
