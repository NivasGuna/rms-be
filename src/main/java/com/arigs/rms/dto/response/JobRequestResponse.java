package com.arigs.rms.dto.response;

import com.arigs.rms.entity.EmploymentType;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.entity.Priority;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Job request response DTO.
 */
public record JobRequestResponse(
        UUID id,
        String requestNumber,
        String clientName,
        String projectName,
        String jobTitle,
        String description,
        String location,
        EmploymentType employmentType,
        Priority priority,
        int numberOfPositions,
        int minExperienceYears,
        int maxExperienceYears,
        BigDecimal budgetAmount,
        LocalDate targetDate,
        JobRequestStatus status,
        UUID salesOwnerId,
        String salesOwnerName,
        UUID businessApproverId,
        String businessApproverName,
        UUID tagManagerId,
        String tagManagerName,
        UUID tagAssociateId,
        String tagAssociateName,
        boolean active
) {
}
