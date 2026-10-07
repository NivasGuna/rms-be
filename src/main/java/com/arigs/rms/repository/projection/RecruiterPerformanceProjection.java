package com.arigs.rms.repository.projection;

import java.util.UUID;

/**
 * Scalar projection for recruiter performance.
 */
public interface RecruiterPerformanceProjection {

    UUID getRecruiterId();

    String getRecruiterName();

    long getSubmittedCandidates();

    long getJoinedCandidates();
}
