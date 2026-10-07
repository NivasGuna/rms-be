package com.arigs.rms.service.impl;

import com.arigs.rms.dto.response.WorkflowTimelineResponse;
import com.arigs.rms.entity.Candidate;
import com.arigs.rms.entity.CandidateStatusHistory;
import com.arigs.rms.entity.Interview;
import com.arigs.rms.entity.JobStatusHistory;
import com.arigs.rms.repository.CandidateRepository;
import com.arigs.rms.repository.CandidateStatusHistoryRepository;
import com.arigs.rms.repository.InterviewRepository;
import com.arigs.rms.repository.JobStatusHistoryRepository;
import com.arigs.rms.repository.JoiningRepository;
import com.arigs.rms.repository.OfferRepository;
import com.arigs.rms.service.WorkflowTimelineService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Builds unified business timelines from persisted workflow history.
 */
@Service
@RequiredArgsConstructor
public class WorkflowTimelineServiceImpl implements WorkflowTimelineService {

    private final JobStatusHistoryRepository jobStatusHistoryRepository;
    private final CandidateStatusHistoryRepository candidateStatusHistoryRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private final OfferRepository offerRepository;
    private final JoiningRepository joiningRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowTimelineResponse> jobTimeline(UUID jobRequestId) {
        List<WorkflowTimelineResponse> timeline = new ArrayList<>();
        jobStatusHistoryRepository.findByJobRequestIdOrderByCreatedDateAsc(jobRequestId).stream()
                .map(this::fromJobHistory)
                .forEach(timeline::add);
        candidateRepository.findByJobRequestIdAndActiveTrueAndDeletedFalse(jobRequestId).stream()
                .map(Candidate::getId)
                .flatMap(candidateId -> candidateTimeline(candidateId).stream())
                .forEach(timeline::add);
        return sorted(timeline);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowTimelineResponse> candidateTimeline(UUID candidateId) {
        List<WorkflowTimelineResponse> timeline = new ArrayList<>();
        candidateStatusHistoryRepository.findByCandidateIdOrderByCreatedDateAsc(candidateId).stream()
                .map(this::fromCandidateHistory)
                .forEach(timeline::add);
        interviewRepository.findByCandidateIdAndActiveTrueOrderByScheduledAtAsc(candidateId).stream()
                .map(this::fromInterview)
                .forEach(timeline::add);
        offerRepository.findByCandidateIdAndActiveTrue(candidateId)
                .map(offer -> new WorkflowTimelineResponse(
                        offer.getCreatedDate(),
                        "OFFER",
                        "Offer",
                        offer.getId(),
                        null,
                        offer.getStatus().name(),
                        offer.getCreatedBy(),
                        "Offer amount: " + offer.getOfferAmount()))
                .ifPresent(timeline::add);
        joiningRepository.findByCandidateIdAndActiveTrue(candidateId)
                .map(joining -> new WorkflowTimelineResponse(
                        joining.getCreatedDate(),
                        "JOINING",
                        "Joining",
                        joining.getId(),
                        null,
                        joining.getStatus().name(),
                        joining.getCreatedBy(),
                        joining.getRemarks()))
                .ifPresent(timeline::add);
        return sorted(timeline);
    }

    private WorkflowTimelineResponse fromJobHistory(JobStatusHistory history) {
        return new WorkflowTimelineResponse(
                history.getCreatedDate(),
                "JOB_STATUS_CHANGE",
                "JobRequest",
                history.getJobRequest().getId(),
                history.getOldStatus() == null ? null : history.getOldStatus().name(),
                history.getNewStatus().name(),
                history.getCreatedBy(),
                history.getComments());
    }

    private WorkflowTimelineResponse fromCandidateHistory(CandidateStatusHistory history) {
        return new WorkflowTimelineResponse(
                history.getCreatedDate(),
                "CANDIDATE_STATUS_CHANGE",
                "Candidate",
                history.getCandidate().getId(),
                history.getOldStatus() == null ? null : history.getOldStatus().name(),
                history.getNewStatus().name(),
                history.getCreatedBy(),
                history.getComments());
    }

    private WorkflowTimelineResponse fromInterview(Interview interview) {
        return new WorkflowTimelineResponse(
                interview.getCreatedDate(),
                "INTERVIEW",
                "Interview",
                interview.getId(),
                null,
                interview.getResult().name(),
                interview.getCreatedBy(),
                interview.getRoundName() + " scheduled at " + interview.getScheduledAt());
    }

    private List<WorkflowTimelineResponse> sorted(List<WorkflowTimelineResponse> timeline) {
        return timeline.stream()
                .sorted(Comparator.naturalOrder())
                .toList();
    }
}
