package com.arigs.rms.dto.response;

import com.arigs.rms.entity.BackgroundJobStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * Background job execution response.
 */
public record BackgroundJobExecutionResponse(
        UUID id,
        String jobCode,
        String handlerName,
        BackgroundJobStatus status,
        Instant startedAt,
        Instant finishedAt,
        String failureReason
) {
}
