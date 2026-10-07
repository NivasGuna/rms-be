package com.arigs.rms.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

/**
 * Standard API response envelope returned by controllers.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Instant timestamp,
        int status,
        String message,
        T data,
        List<ApiError> errors
) {

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(Instant.now(), status, message, data, List.of());
    }

    public static <T> ApiResponse<T> failure(int status, String message, List<ApiError> errors) {
        return new ApiResponse<>(Instant.now(), status, message, null, errors);
    }
}
