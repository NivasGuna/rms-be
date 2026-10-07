package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Notification type master.
 */
@Getter
@Setter
@Entity
@Table(name = "notification_types", indexes = {
        @Index(name = "idx_notification_types_code", columnList = "code")
})
public class NotificationType extends BaseMasterEntity {
}
