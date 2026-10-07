package com.arigs.rms.integration;

import static org.mockito.Mockito.verify;

import com.arigs.rms.entity.OutboxEvent;
import com.arigs.rms.event.DomainEvent;
import com.arigs.rms.event.DomainEventPublisher;
import com.arigs.rms.outbox.OutboxDispatchService;
import com.arigs.rms.outbox.OutboxEventStatus;
import com.arigs.rms.repository.OutboxEventRepository;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:rms_outbox;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false",
        "spring.quartz.job-store-type=memory",
        "spring.task.scheduling.enabled=false",
        "management.tracing.enabled=false",
        "rms.security.jwt-secret=0123456789012345678901234567890101234567890123456789012345678901",
        "rms.files.storage-root=target/test-storage/rms-outbox-test",
        "rms.platform.feature-flags.outbox-dispatch=true",
        "rms.platform.feature-flags.external-integrations=true"
})
class OutboxIntegrationTest {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private OutboxDispatchService outboxDispatchService;

    @MockBean
    private IntegrationEventPublisher integrationEventPublisher;

    @Test
    void persistsAndDispatchesDomainEventThroughOutbox() {
        DomainEvent event = DomainEvent.of("platform.test", "Platform", "test-1", Map.of("source", "integration-test"));

        domainEventPublisher.publish(event);

        List<OutboxEvent> events = outboxEventRepository.findAll();
        OutboxEvent pending = events.stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected at least one outbox event to be persisted"));
        Assertions.assertThat(pending.getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        Assertions.assertThat(pending.getPayload()).contains("platform.test");

        int dispatched = outboxDispatchService.dispatchDueEvents();

        Assertions.assertThat(dispatched).isEqualTo(1);
        List<OutboxEvent> updatedEvents = outboxEventRepository.findAll();
        OutboxEvent published = updatedEvents.stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected at least one outbox event after dispatch"));
        Assertions.assertThat(published.getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        ArgumentCaptor<OutboxEvent> captor = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(integrationEventPublisher).publish(captor.capture());
        Assertions.assertThat(captor.getValue().getEventKey()).isEqualTo(event.eventId().toString());
    }
}
