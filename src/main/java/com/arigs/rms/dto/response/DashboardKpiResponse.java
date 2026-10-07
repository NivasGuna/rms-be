package com.arigs.rms.dto.response;

/**
 * Dashboard KPI summary.
 */
public record DashboardKpiResponse(
        long totalJobs,
        long openJobs,
        long inProgressJobs,
        long pendingApprovals,
        long closedJobs,
        long openPositions,
        long activeCandidates,
        long interviewsScheduled,
        long offersReleased,
        long joinedCandidates
) {
}
