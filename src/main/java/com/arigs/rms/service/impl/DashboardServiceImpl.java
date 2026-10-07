package com.arigs.rms.service.impl;

import com.arigs.rms.dto.response.DashboardKpiResponse;
import com.arigs.rms.dto.response.MonthlyHiringResponse;
import com.arigs.rms.dto.response.RecruiterPerformanceResponse;
import com.arigs.rms.dto.response.StatusCountResponse;
import com.arigs.rms.entity.CandidateStatus;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.entity.JoiningStatus;
import com.arigs.rms.entity.OfferStatus;
import com.arigs.rms.repository.CandidateRepository;
import com.arigs.rms.repository.InterviewRepository;
import com.arigs.rms.repository.JobRequestRepository;
import com.arigs.rms.repository.JoiningRepository;
import com.arigs.rms.repository.OfferRepository;
import com.arigs.rms.repository.projection.StatusCountProjection;
import com.arigs.rms.service.DashboardService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Optimized dashboard service backed by projection queries.
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final JobRequestRepository jobRequestRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private final OfferRepository offerRepository;
    private final JoiningRepository joiningRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardKpiResponse kpis() {
        long totalJobs = jobRequestRepository.countByActiveTrueAndDeletedFalse();

        long openJobs = jobRequestRepository.countByStatusInAndActiveTrueAndDeletedFalse(List.of(
                JobRequestStatus.BUSINESS_APPROVED,
                JobRequestStatus.TAG_MANAGER_ASSIGNED,
                JobRequestStatus.TAG_ASSOCIATE_ASSIGNED,
                JobRequestStatus.CANDIDATE_SOURCING,
                JobRequestStatus.INTERVIEW_IN_PROGRESS,
                JobRequestStatus.OFFER_RELEASED));

        long inProgressJobs = jobRequestRepository.countByStatusInAndActiveTrueAndDeletedFalse(List.of(
                JobRequestStatus.TAG_ASSOCIATE_ASSIGNED,
                JobRequestStatus.CANDIDATE_SOURCING,
                JobRequestStatus.INTERVIEW_IN_PROGRESS));

        long pendingApprovals = jobRequestRepository.countByStatusInAndActiveTrueAndDeletedFalse(List.of(
                JobRequestStatus.DRAFT,
                JobRequestStatus.PENDING_BUSINESS_APPROVAL));

        long closedJobs = jobRequestRepository.countByStatusInAndActiveTrueAndDeletedFalse(List.of(
                JobRequestStatus.CLOSED,
                JobRequestStatus.CANCELLED,
                JobRequestStatus.JOINED));

        return new DashboardKpiResponse(
                totalJobs,
                openJobs,
                inProgressJobs,
                pendingApprovals,
                closedJobs,
                jobRequestRepository.sumOpenPositions(),
                candidateRepository.countByActiveTrueAndDeletedFalse(),
                interviewRepository.countByActiveTrueAndDeletedFalse(),
                offerRepository.countByStatusAndActiveTrueAndDeletedFalse(OfferStatus.RELEASED),
                joiningRepository.countByStatusAndActiveTrueAndDeletedFalse(JoiningStatus.JOINED));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusCountResponse> candidatePipeline() {
        return mapStatusCounts(candidateRepository.countByStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusCountResponse> interviewPipeline() {
        return mapStatusCounts(interviewRepository.countByResult());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusCountResponse> offerPipeline() {
        return mapStatusCounts(offerRepository.countByStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusCountResponse> joiningStatistics() {
        return mapStatusCounts(joiningRepository.countByStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyHiringResponse> monthlyHiring(LocalDate from, LocalDate to) {
        LocalDate safeTo = to == null ? LocalDate.now() : to;
        LocalDate safeFrom = from == null ? safeTo.minusMonths(11).withDayOfMonth(1) : from;
        return joiningRepository.monthlyHiring(safeFrom, safeTo).stream()
                .map(row -> new MonthlyHiringResponse(row.getYear(), row.getMonth(), row.getTotal()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterPerformanceResponse> recruiterPerformance() {
        return candidateRepository.recruiterPerformance().stream()
                .map(row -> new RecruiterPerformanceResponse(
                        row.getRecruiterId(),
                        row.getRecruiterName(),
                        row.getSubmittedCandidates(),
                        row.getJoinedCandidates()))
                .toList();
    }

    private List<StatusCountResponse> mapStatusCounts(List<StatusCountProjection> rows) {
        return rows.stream()
                .map(row -> new StatusCountResponse(String.valueOf(row.getStatus()), row.getTotal()))
                .toList();
    }
}
