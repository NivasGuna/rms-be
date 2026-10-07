package com.arigs.rms.scheduler;

import com.arigs.rms.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Retries failed email notifications.
 */
@Component
@RequiredArgsConstructor
public class NotificationRetryScheduler {

    private static final Logger log = LoggerFactory.getLogger(NotificationRetryScheduler.class);

    private final NotificationService notificationService;

    @Scheduled(fixedDelayString = "${rms.notification.retry-delay-ms:300000}")
    public void retryFailedNotifications() {
        int retried = notificationService.retryFailedNotifications();
        if (retried > 0) {
            log.info("Retried {} failed notifications", retried);
        }
    }
}
