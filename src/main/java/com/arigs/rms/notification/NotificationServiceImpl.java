package com.arigs.rms.notification;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.dto.request.EmailRequest;
import com.arigs.rms.email.EmailService;
import com.arigs.rms.entity.NotificationLog;
import com.arigs.rms.entity.NotificationStatus;
import com.arigs.rms.repository.NotificationLogRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persists and delivers email notifications with retry support.
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationLogRepository notificationLogRepository;
    private final EmailService emailService;
    private final RmsProperties properties;

    @Override
    @Transactional
    public void sendEmail(EmailRequest request) {
        NotificationLog log = new NotificationLog();
        log.setRecipientEmail(request.recipientEmail());
        log.setSubject(request.subject());
        log.setHtmlBody(request.htmlTemplate());
        deliver(log, request);
    }

    @Override
    @Transactional
    public int retryFailedNotifications() {
        var failed = notificationLogRepository.findByStatusAndRetryCountLessThanAndActiveTrue(
                NotificationStatus.FAILED, properties.getNotification().getRetryLimit(), PageRequest.of(0, 25));
        failed.forEach(log -> deliver(log, new EmailRequest(log.getRecipientEmail(), log.getSubject(), log.getHtmlBody(), java.util.Map.of())));
        return failed.size();
    }

    private void deliver(NotificationLog log, EmailRequest request) {
        try {
            emailService.sendHtmlEmail(request.recipientEmail(), request.subject(), request.htmlTemplate(), request.variables());
            log.setStatus(NotificationStatus.SENT);
            log.setSentAt(Instant.now());
            log.setLastError(null);
        } catch (RuntimeException ex) {
            log.setStatus(NotificationStatus.FAILED);
            log.setRetryCount(log.getRetryCount() + 1);
            log.setLastError(ex.getMessage());
        }
        notificationLogRepository.save(log);
    }
}
