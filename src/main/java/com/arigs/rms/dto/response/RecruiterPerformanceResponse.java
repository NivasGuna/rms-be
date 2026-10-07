package com.arigs.rms.dto.response;

import java.util.UUID;

/**
 * Recruiter performance dashboard row.
 */
public record RecruiterPerformanceResponse(
        UUID recruiterId,
        String recruiterName,
        long submittedCandidates,
        long joinedCandidates
) {
}
