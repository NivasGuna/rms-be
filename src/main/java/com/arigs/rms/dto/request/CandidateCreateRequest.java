package com.arigs.rms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request to create a candidate for a job request.
 */
public record CandidateCreateRequest(
        @NotNull UUID jobRequestId,
        @NotBlank @Size(max = 40) String candidateCode,
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 30) String phone,
        @Size(max = 150) String currentCompany,
        @Min(0) @Max(60) int totalExperienceYears,
        BigDecimal expectedCtc,
        BigDecimal currentCtc,
        @Min(0) @Max(365) Integer noticePeriodDays
) {
}
