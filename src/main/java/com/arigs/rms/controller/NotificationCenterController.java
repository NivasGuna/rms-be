package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.NotificationCreateRequest;
import com.arigs.rms.dto.request.NotificationPreferenceRequest;
import com.arigs.rms.dto.response.InAppNotificationResponse;
import com.arigs.rms.service.NotificationCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notification center API.
 */
@RestController
@RequestMapping("/api/v1/notification-center")
@RequiredArgsConstructor
@Tag(name = "Notification Center")
public class NotificationCenterController {

    private final NotificationCenterService notificationCenterService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER')")
    @Operation(summary = "Publish in-app notification")
    public ResponseEntity<ApiResponse<Void>> publish(@Valid @RequestBody NotificationCreateRequest request) {
        notificationCenterService.publish(request);
        return ApiResponseBuilder.accepted("Notification published", null);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get user notification inbox")
    public ResponseEntity<ApiResponse<PageResponse<InAppNotificationResponse>>> inbox(@PathVariable UUID userId, Pageable pageable) {
        return ApiResponseBuilder.ok("Notifications retrieved", notificationCenterService.inbox(userId, pageable));
    }

    @GetMapping("/users/{userId}/unread-count")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get unread notification count")
    public ResponseEntity<ApiResponse<Long>> unreadCount(@PathVariable UUID userId) {
        return ApiResponseBuilder.ok("Unread notification count retrieved", notificationCenterService.unreadCount(userId));
    }

    @PostMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<ApiResponse<InAppNotificationResponse>> markRead(@PathVariable UUID notificationId) {
        return ApiResponseBuilder.ok("Notification marked read", notificationCenterService.markRead(notificationId));
    }

    @PostMapping("/preferences")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Save notification preference")
    public ResponseEntity<ApiResponse<Void>> savePreference(@Valid @RequestBody NotificationPreferenceRequest request) {
        notificationCenterService.savePreference(request);
        return ApiResponseBuilder.ok("Notification preference saved", null);
    }
}
