package com.arigs.rms.repository;

import com.arigs.rms.entity.NotificationLog;
import com.arigs.rms.entity.NotificationStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for notification logs.
 */
public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {

    List<NotificationLog> findByStatusAndRetryCountLessThanAndActiveTrue(NotificationStatus status, int retryLimit, Pageable pageable);
}
