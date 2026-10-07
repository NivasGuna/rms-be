package com.arigs.rms.entity;

import com.arigs.rms.outbox.OutboxEventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Durable domain event waiting for external dispatch.
 */
@Getter
@Setter
@Entity
@Table(name = "outbox_events", indexes = {
        @Index(name = "idx_outbox_events_status_created", columnList = "status, created_date"),
        @Index(name = "idx_outbox_events_aggregate", columnList = "aggregate_type, aggregate_id")
})
public class OutboxEvent extends BaseAuditableEntity {

    @Column(name = "event_key", nullable = false, unique = true, length = 120)
    private String eventKey;

    @Column(name = "event_type", nullable = false, length = 120)
    private String eventType;

    @Column(name = "aggregate_type", nullable = false, length = 80)
    private String aggregateType;

    @Column(name = "aggregate_id", length = 120)
    private String aggregateId;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(nullable = false, columnDefinition = "text")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OutboxEventStatus status = OutboxEventStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;
}
