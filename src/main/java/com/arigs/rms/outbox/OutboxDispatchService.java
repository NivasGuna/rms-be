package com.arigs.rms.outbox;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.entity.OutboxEvent;
import com.arigs.rms.feature.FeatureFlagService;
import com.arigs.rms.integration.IntegrationEventPublisher;
import com.arigs.rms.repository.OutboxEventRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dispatches durable outbox events to the configured integration publisher.
 */
@Service
@RequiredArgsConstructor
public class OutboxDispatchService {

    private final OutboxEventRepository repository;
    private final IntegrationEventPublisher integrationEventPublisher;
    private final RmsProperties properties;
    private final FeatureFlagService featureFlagService;

    @Transactional
    public int dispatchDueEvents() {
        if (!properties.getOutbox().isEnabled() || !featureFlagService.isEnabled("outbox-dispatch")) {
            return 0;
        }
        List<OutboxEvent> events = repository.findDispatchable(
                List.of(OutboxEventStatus.PENDING, OutboxEventStatus.FAILED),
                properties.getOutbox().getRetryLimit(),
                PageRequest.of(0, properties.getOutbox().getBatchSize()));
        events.forEach(this::dispatch);
        return events.size();
    }

    private void dispatch(OutboxEvent event) {
        event.setLastAttemptAt(Instant.now());
        try {
            integrationEventPublisher.publish(event);
            event.setStatus(OutboxEventStatus.PUBLISHED);
            event.setPublishedAt(Instant.now());
            event.setLastError(null);
        } catch (RuntimeException ex) {
            event.setStatus(OutboxEventStatus.FAILED);
            event.setRetryCount(event.getRetryCount() + 1);
            event.setLastError(ex.getMessage());
        }
    }
}
