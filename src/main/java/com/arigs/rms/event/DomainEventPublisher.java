package com.arigs.rms.event;

/**
 * Publishes domain events to local listeners and the durable outbox.
 */
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
