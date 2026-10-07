package com.arigs.rms.health;

import com.arigs.rms.config.RmsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Reports outbound integration configuration status.
 */
@Component("integrations")
@RequiredArgsConstructor
public class IntegrationsHealthIndicator implements HealthIndicator {

    private final RmsProperties properties;

    @Override
    public Health health() {
        return Health.up()
                .withDetail("outboundEnabled", properties.getIntegrations().isOutboundEnabled())
                .withDetail("timeout", properties.getIntegrations().getTimeout().toString())
                .withDetail("configuredEndpoints", properties.getIntegrations().getEndpoints().keySet())
                .build();
    }
}
