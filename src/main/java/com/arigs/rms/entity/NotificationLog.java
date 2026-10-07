package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Persistent log of outbound email notifications.
 */
@Getter
@Setter
@Entity
@Table(name = "notification_logs", indexes = {
        @Index(name = "idx_notification_logs_status", columnList = "status"),
        @Index(name = "idx_notification_logs_recipient", columnList = "recipient_email")
})
public class NotificationLog extends BaseAuditableEntity {

    @Column(name = "recipient_email", nullable = false, length = 150)
    private String recipientEmail;

    @Column(nullable = false, length = 180)
    private String subject;

    @Column(name = "html_body", nullable = false, columnDefinition = "text")
    private String htmlBody;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    @Column(name = "sent_at")
    private Instant sentAt;
}
