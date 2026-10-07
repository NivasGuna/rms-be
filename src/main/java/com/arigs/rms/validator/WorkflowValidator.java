package com.arigs.rms.validator;

import com.arigs.rms.entity.CandidateStatus;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.exception.BusinessException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Centralized workflow transition validation.
 */
@Component
public class WorkflowValidator {

    private final Map<JobRequestStatus, Set<JobRequestStatus>> jobTransitions = new EnumMap<>(JobRequestStatus.class);
    private final Map<CandidateStatus, Set<CandidateStatus>> candidateTransitions = new EnumMap<>(CandidateStatus.class);

    public WorkflowValidator() {
        jobTransitions.put(JobRequestStatus.DRAFT,
                EnumSet.of(JobRequestStatus.PENDING_BUSINESS_APPROVAL, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.PENDING_BUSINESS_APPROVAL,
                EnumSet.of(JobRequestStatus.BUSINESS_APPROVED, JobRequestStatus.BUSINESS_REJECTED, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.BUSINESS_APPROVED,
                EnumSet.of(JobRequestStatus.TAG_MANAGER_ASSIGNED, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.TAG_MANAGER_ASSIGNED,
                EnumSet.of(JobRequestStatus.TAG_ASSOCIATE_ASSIGNED, JobRequestStatus.CANDIDATE_SOURCING, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.TAG_ASSOCIATE_ASSIGNED,
                EnumSet.of(JobRequestStatus.CANDIDATE_SOURCING, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.CANDIDATE_SOURCING,
                EnumSet.of(JobRequestStatus.INTERVIEW_IN_PROGRESS, JobRequestStatus.CLOSED, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.INTERVIEW_IN_PROGRESS,
                EnumSet.of(JobRequestStatus.OFFER_RELEASED, JobRequestStatus.CLOSED, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.OFFER_RELEASED,
                EnumSet.of(JobRequestStatus.JOINED, JobRequestStatus.CLOSED, JobRequestStatus.CANCELLED));
        jobTransitions.put(JobRequestStatus.JOINED,
                EnumSet.of(JobRequestStatus.CLOSED));

        candidateTransitions.put(CandidateStatus.UPLOADED,
                EnumSet.of(CandidateStatus.SCREENING, CandidateStatus.REJECTED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.SCREENING,
                EnumSet.of(CandidateStatus.SHORTLISTED, CandidateStatus.REJECTED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.SHORTLISTED,
                EnumSet.of(CandidateStatus.INTERVIEW_SCHEDULED, CandidateStatus.REJECTED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.INTERVIEW_SCHEDULED,
                EnumSet.of(CandidateStatus.INTERVIEWED, CandidateStatus.REJECTED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.INTERVIEWED,
                EnumSet.of(CandidateStatus.SELECTED, CandidateStatus.REJECTED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.SELECTED,
                EnumSet.of(CandidateStatus.OFFER_RELEASED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.OFFER_RELEASED,
                EnumSet.of(CandidateStatus.OFFER_ACCEPTED, CandidateStatus.OFFER_DECLINED, CandidateStatus.DROPPED));
        candidateTransitions.put(CandidateStatus.OFFER_ACCEPTED,
                EnumSet.of(CandidateStatus.JOINED, CandidateStatus.DROPPED));
    }

    public void validateJobTransition(JobRequestStatus current, JobRequestStatus next) {
        if (current == next) {
            return;
        }
        if (!jobTransitions.getOrDefault(current, Set.of()).contains(next)) {
            throw new BusinessException("Invalid job request transition from " + current + " to " + next);
        }
    }

    public void validateCandidateTransition(CandidateStatus current, CandidateStatus next) {
        if (current == next) {
            return;
        }
        if (!candidateTransitions.getOrDefault(current, Set.of()).contains(next)) {
            throw new BusinessException("Invalid candidate transition from " + current + " to " + next);
        }
    }
}
