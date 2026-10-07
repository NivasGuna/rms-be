package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.dto.request.EmailRequest;
import com.arigs.rms.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notification administration API.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/email")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Send a templated email notification")
    public ResponseEntity<ApiResponse<Void>> sendEmail(@Valid @RequestBody EmailRequest request) {
        notificationService.sendEmail(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(HttpStatus.ACCEPTED.value(), "Email notification accepted", null));
    }

    @PostMapping("/retry")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Retry failed email notifications")
    public ResponseEntity<ApiResponse<Integer>> retryFailed() {
        int retried = notificationService.retryFailedNotifications();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Notification retry completed", retried));
    }
}
