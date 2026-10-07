package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.NotificationCreateRequest;
import com.arigs.rms.dto.request.NotificationPreferenceRequest;
import com.arigs.rms.dto.response.InAppNotificationResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * In-app notification center use cases.
 */
public interface NotificationCenterService {

    void publish(NotificationCreateRequest request);

    PageResponse<InAppNotificationResponse> inbox(UUID userId, Pageable pageable);

    long unreadCount(UUID userId);

    InAppNotificationResponse markRead(UUID notificationId);

    void savePreference(NotificationPreferenceRequest request);
}
