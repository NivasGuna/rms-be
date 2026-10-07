package com.arigs.rms.integration;

import com.arigs.rms.entity.OutboxEvent;

/**
 * Abstraction for dispatching outbox events to external systems.
 */
public interface IntegrationEventPublisher {

    void publish(OutboxEvent event);
}
