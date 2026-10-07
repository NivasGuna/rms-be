package com.arigs.rms.health;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.outbox.OutboxEventStatus;
import com.arigs.rms.repository.OutboxEventRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Reports outbox backlog and stale pending events for readiness checks.
 */
@Component("outbox")
@RequiredArgsConstructor
public class OutboxHealthIndicator implements HealthIndicator {

    private final OutboxEventRepository repository;
    private final RmsProperties properties;

    @Override
    public Health health() {
        long pending = repository.countByStatusAndActiveTrueAndDeletedFalse(OutboxEventStatus.PENDING);
        long failed = repository.countByStatusAndActiveTrueAndDeletedFalse(OutboxEventStatus.FAILED);
        Instant staleThreshold = Instant.now().minus(properties.getOutbox().getStaleAfter());
        long stale = repository.countByStatusAndCreatedDateBeforeAndActiveTrueAndDeletedFalse(
                OutboxEventStatus.PENDING, staleThreshold);
        Health.Builder builder = stale > 0 ? Health.down() : Health.up();
        return builder
                .withDetail("enabled", properties.getOutbox().isEnabled())
                .withDetail("pending", pending)
                .withDetail("failed", failed)
                .withDetail("stalePending", stale)
                .build();
    }
}
