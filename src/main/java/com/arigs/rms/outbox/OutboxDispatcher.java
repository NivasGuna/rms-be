package com.arigs.rms.outbox;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically drains the durable outbox.
 */
@Component
@RequiredArgsConstructor
public class OutboxDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxDispatcher.class);

    private final OutboxDispatchService outboxDispatchService;

    @Scheduled(fixedDelayString = "${rms.outbox.dispatch-delay-ms:30000}")
    public void dispatch() {
        int dispatched = outboxDispatchService.dispatchDueEvents();
        if (dispatched > 0) {
            log.info("Dispatched {} outbox events", dispatched);
        }
    }
}
