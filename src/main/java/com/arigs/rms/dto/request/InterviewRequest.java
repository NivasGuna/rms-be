package com.arigs.rms.dto.request;

import com.arigs.rms.entity.InterviewResult;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/**
 * Request to schedule or update an interview.
 */
public record InterviewRequest(
        @NotBlank @Size(max = 80) String roundName,
        @NotNull @FutureOrPresent Instant scheduledAt,
        @NotBlank @Size(max = 40) String mode,
        @NotBlank @Size(max = 150) String interviewerName,
        String feedback,
        InterviewResult result
) {
}
