package com.arigs.rms.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Immutable domain event emitted by RMS business workflows.
 */
public record DomainEvent(
        UUID eventId,
        String eventType,
        String aggregateType,
        String aggregateId,
        Instant occurredAt,
        Map<String, Object> payload
) {

    public static DomainEvent of(String eventType, String aggregateType, String aggregateId, Map<String, Object> payload) {
        return new DomainEvent(UUID.randomUUID(), eventType, aggregateType, aggregateId, Instant.now(), Map.copyOf(payload));
    }
}
