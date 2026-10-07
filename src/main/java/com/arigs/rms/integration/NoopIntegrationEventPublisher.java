package com.arigs.rms.integration;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.entity.OutboxEvent;
import com.arigs.rms.feature.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Production-safe default publisher that records successful handoff without external side effects.
 */
@Component
@RequiredArgsConstructor
public class NoopIntegrationEventPublisher implements IntegrationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(NoopIntegrationEventPublisher.class);

    private final RmsProperties properties;
    private final FeatureFlagService featureFlagService;

    @Override
    public void publish(OutboxEvent event) {
        if (!properties.getIntegrations().isOutboundEnabled() || !featureFlagService.isEnabled("external-integrations")) {
            log.debug("Outbound integrations disabled; acknowledged outbox event {}", event.getEventKey());
            return;
        }
        log.info("Outbox event {} ({}) acknowledged by default integration publisher",
                event.getEventKey(), event.getEventType());
    }
}
