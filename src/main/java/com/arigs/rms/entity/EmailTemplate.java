package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Database-driven email template.
 */
@Getter
@Setter
@Entity
@Table(name = "email_templates", indexes = {
        @Index(name = "idx_email_templates_code", columnList = "code"),
        @Index(name = "idx_email_templates_notification_type", columnList = "notification_type_id")
})
public class EmailTemplate extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    @Column(nullable = false, length = 180)
    private String subject;

    @Column(name = "html_body", nullable = false, columnDefinition = "text")
    private String htmlBody;
}
