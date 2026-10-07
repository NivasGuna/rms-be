package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Durable in-app notification for a user.
 */
@Getter
@Setter
@Entity
@Table(name = "in_app_notifications", indexes = {
        @Index(name = "idx_in_app_notifications_user_read", columnList = "recipient_user_id,read_at"),
        @Index(name = "idx_in_app_notifications_created", columnList = "created_date")
})
public class InAppNotification extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_user_id", nullable = false)
    private AppUser recipientUser;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "notification_type", nullable = false, length = 60)
    private String notificationType;

    @Column(name = "entity_type", length = 80)
    private String entityType;

    @Column(name = "entity_id", length = 80)
    private String entityId;

    @Column(name = "read_at")
    private Instant readAt;
}
