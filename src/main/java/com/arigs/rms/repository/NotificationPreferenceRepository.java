package com.arigs.rms.repository;

import com.arigs.rms.entity.NotificationPreference;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for notification preferences.
 */
public interface NotificationPreferenceRepository extends BaseRepository<NotificationPreference> {

    Optional<NotificationPreference> findByUserIdAndNotificationTypeAndActiveTrueAndDeletedFalse(UUID userId, String notificationType);
}
