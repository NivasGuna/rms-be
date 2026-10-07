package com.arigs.rms.dto.request;

import com.arigs.rms.entity.EmploymentType;
import com.arigs.rms.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request to create a job request.
 */
public record JobRequestCreateRequest(
        @NotBlank @Size(max = 40) String requestNumber,
        @NotBlank @Size(max = 150) String clientName,
        @NotBlank @Size(max = 150) String projectName,
        @NotBlank @Size(max = 150) String jobTitle,
        @NotBlank String description,
        @NotBlank @Size(max = 120) String location,
        @NotNull EmploymentType employmentType,
        @NotNull Priority priority,
        @Positive int numberOfPositions,
        @Min(0) @Max(60) int minExperienceYears,
        @Min(0) @Max(60) int maxExperienceYears,
        BigDecimal budgetAmount,
        @NotNull @FutureOrPresent LocalDate targetDate,
        @NotNull UUID salesOwnerId
) {
}
