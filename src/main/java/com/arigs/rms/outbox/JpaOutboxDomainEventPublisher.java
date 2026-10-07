package com.arigs.rms.outbox;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.entity.OutboxEvent;
import com.arigs.rms.event.DomainEvent;
import com.arigs.rms.event.DomainEventPublisher;
import com.arigs.rms.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Stores events in the outbox and publishes them to in-process listeners.
 */
@Component
@RequiredArgsConstructor
public class JpaOutboxDomainEventPublisher implements DomainEventPublisher {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RmsProperties properties;

    @Override
    @Transactional
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
        if (!properties.getOutbox().isEnabled()) {
            return;
        }
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventKey(event.eventId().toString());
        outboxEvent.setEventType(event.eventType());
        outboxEvent.setAggregateType(event.aggregateType());
        outboxEvent.setAggregateId(event.aggregateId());
        outboxEvent.setOccurredAt(event.occurredAt());
        outboxEvent.setPayload(serialize(event));
        repository.save(outboxEvent);
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Domain event payload could not be serialized", ex);
        }
    }
}
