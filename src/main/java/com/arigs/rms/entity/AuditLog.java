package com.arigs.rms.entity;

import com.arigs.rms.audit.AuditEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistent application audit event.
 */
@Getter
@Setter
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_logs_event_type", columnList = "event_type"),
        @Index(name = "idx_audit_logs_actor", columnList = "actor")
})
public class AuditLog extends BaseAuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private AuditEventType eventType;

    @Column(nullable = false, length = 100)
    private String actor;

    @Column(nullable = false, length = 120)
    private String action;

    @Column(name = "entity_type", length = 80)
    private String entityType;

    @Column(name = "entity_id", length = 80)
    private String entityId;

    @Column(columnDefinition = "text")
    private String details;
}
