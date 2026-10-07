package com.arigs.rms.notification;

import com.arigs.rms.dto.request.EmailRequest;

/**
 * Notification orchestration use cases.
 */
public interface NotificationService {

    void sendEmail(EmailRequest request);

    int retryFailedNotifications();
}
