package com.arigs.rms.dto.request;

import com.arigs.rms.entity.CandidateStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request to transition a candidate status.
 */
public record CandidateStatusUpdateRequest(
        @NotNull CandidateStatus status,
        @NotBlank String comments
) {
}
