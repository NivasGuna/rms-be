package com.arigs.rms.dto.response;

import com.arigs.rms.entity.InterviewResult;
import java.time.Instant;
import java.util.UUID;

/**
 * Interview response DTO.
 */
public record InterviewResponse(
        UUID id,
        UUID candidateId,
        String roundName,
        Instant scheduledAt,
        String mode,
        String interviewerName,
        String feedback,
        InterviewResult result
) {
}
