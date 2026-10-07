package com.arigs.rms.repository;

import com.arigs.rms.entity.InAppNotification;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository for in-app notifications.
 */
public interface InAppNotificationRepository extends BaseRepository<InAppNotification> {

    Page<InAppNotification> findByRecipientUserIdAndActiveTrueAndDeletedFalseOrderByCreatedDateDesc(UUID recipientUserId, Pageable pageable);

    long countByRecipientUserIdAndReadAtIsNullAndActiveTrueAndDeletedFalse(UUID recipientUserId);
}
