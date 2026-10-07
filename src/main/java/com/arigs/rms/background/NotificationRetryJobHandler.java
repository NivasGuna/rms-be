package com.arigs.rms.background;

import com.arigs.rms.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Background job handler for failed email notification retries.
 */
@Component
@RequiredArgsConstructor
public class NotificationRetryJobHandler implements BackgroundJobHandler {

    private final NotificationService notificationService;

    @Override
    public String handlerName() {
        return "notificationRetryJobHandler";
    }

    @Override
    public void execute() {
        notificationService.retryFailedNotifications();
    }
}
