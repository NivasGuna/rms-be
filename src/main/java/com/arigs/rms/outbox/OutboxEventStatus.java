package com.arigs.rms.outbox;

/**
 * Durable outbox lifecycle states.
 */
public enum OutboxEventStatus {
    PENDING,
    PUBLISHED,
    FAILED
}
