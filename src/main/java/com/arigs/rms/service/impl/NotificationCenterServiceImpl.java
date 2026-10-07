package com.arigs.rms.service.impl;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.NotificationCreateRequest;
import com.arigs.rms.dto.request.NotificationPreferenceRequest;
import com.arigs.rms.dto.response.InAppNotificationResponse;
import com.arigs.rms.entity.AppUser;
import com.arigs.rms.entity.InAppNotification;
import com.arigs.rms.entity.NotificationPreference;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.repository.AppUserRepository;
import com.arigs.rms.repository.InAppNotificationRepository;
import com.arigs.rms.repository.NotificationPreferenceRepository;
import com.arigs.rms.service.NotificationCenterService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Durable notification center implementation.
 */
@Service
@RequiredArgsConstructor
public class NotificationCenterServiceImpl implements NotificationCenterService {

    private final InAppNotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final AppUserRepository userRepository;

    @Override
    @Transactional
    public void publish(NotificationCreateRequest request) {
        for (UUID recipientId : request.recipientUserIds()) {
            AppUser user = userRepository.findByIdAndActiveTrue(recipientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification recipient not found"));
            boolean enabled = preferenceRepository
                    .findByUserIdAndNotificationTypeAndActiveTrueAndDeletedFalse(recipientId, request.notificationType())
                    .map(NotificationPreference::isInAppEnabled)
                    .orElse(true);
            if (enabled) {
                InAppNotification notification = new InAppNotification();
                notification.setRecipientUser(user);
                notification.setTitle(request.title());
                notification.setMessage(request.message());
                notification.setNotificationType(request.notificationType());
                notification.setEntityType(request.entityType());
                notification.setEntityId(request.entityId());
                notificationRepository.save(notification);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InAppNotificationResponse> inbox(UUID userId, Pageable pageable) {
        return PageResponse.from(notificationRepository
                .findByRecipientUserIdAndActiveTrueAndDeletedFalseOrderByCreatedDateDesc(userId, pageable)
                .map(this::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public long unreadCount(UUID userId) {
        return notificationRepository.countByRecipientUserIdAndReadAtIsNullAndActiveTrueAndDeletedFalse(userId);
    }

    @Override
    @Transactional
    public InAppNotificationResponse markRead(UUID notificationId) {
        InAppNotification notification = notificationRepository.findByIdAndActiveTrueAndDeletedFalse(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setReadAt(Instant.now());
        return toResponse(notification);
    }

    @Override
    @Transactional
    public void savePreference(NotificationPreferenceRequest request) {
        AppUser user = userRepository.findByIdAndActiveTrue(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        NotificationPreference preference = preferenceRepository
                .findByUserIdAndNotificationTypeAndActiveTrueAndDeletedFalse(request.userId(), request.notificationType())
                .orElseGet(NotificationPreference::new);
        preference.setUser(user);
        preference.setNotificationType(request.notificationType());
        preference.setEmailEnabled(request.emailEnabled());
        preference.setInAppEnabled(request.inAppEnabled());
        preferenceRepository.save(preference);
    }

    private InAppNotificationResponse toResponse(InAppNotification notification) {
        return new InAppNotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getNotificationType(),
                notification.getEntityType(),
                notification.getEntityId(),
                notification.getReadAt() != null,
                notification.getReadAt(),
                notification.getCreatedDate());
    }
}
