package com.arigs.rms.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Convenience builder for consistent response envelopes.
 */
public final class ApiResponseBuilder {

    private ApiResponseBuilder() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> accepted(String message, T data) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(HttpStatus.ACCEPTED.value(), message, data));
    }
}
