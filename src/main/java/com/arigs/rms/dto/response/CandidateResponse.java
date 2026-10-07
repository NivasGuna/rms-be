package com.arigs.rms.dto.response;

import com.arigs.rms.entity.CandidateStatus;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Candidate response DTO.
 */
public record CandidateResponse(
        UUID id,
        UUID jobRequestId,
        String jobRequestNumber,
        String candidateCode,
        String firstName,
        String lastName,
        String email,
        String phone,
        String currentCompany,
        int totalExperienceYears,
        BigDecimal expectedCtc,
        BigDecimal currentCtc,
        Integer noticePeriodDays,
        CandidateStatus status,
        boolean active
) {
}
