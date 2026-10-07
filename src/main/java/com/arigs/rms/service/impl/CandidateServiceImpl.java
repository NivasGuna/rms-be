package com.arigs.rms.service.impl;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.CandidateCreateRequest;
import com.arigs.rms.dto.request.CandidateStatusUpdateRequest;
import com.arigs.rms.dto.request.InterviewRequest;
import com.arigs.rms.dto.request.JoiningRequest;
import com.arigs.rms.dto.request.OfferRequest;
import com.arigs.rms.dto.response.CandidateResponse;
import com.arigs.rms.dto.response.InterviewResponse;
import com.arigs.rms.dto.response.JoiningResponse;
import com.arigs.rms.dto.response.OfferResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.Candidate;
import com.arigs.rms.entity.CandidateStatus;
import com.arigs.rms.entity.CandidateStatusHistory;
import com.arigs.rms.entity.Interview;
import com.arigs.rms.entity.InterviewResult;
import com.arigs.rms.entity.JobRequest;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.entity.JobStatusHistory;
import com.arigs.rms.entity.Joining;
import com.arigs.rms.entity.JoiningStatus;
import com.arigs.rms.entity.Offer;
import com.arigs.rms.entity.OfferStatus;
import com.arigs.rms.event.DomainEvent;
import com.arigs.rms.event.DomainEventPublisher;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.exception.DuplicateResourceException;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.mapper.CandidateMapper;
import com.arigs.rms.repository.CandidateRepository;
import com.arigs.rms.repository.CandidateStatusHistoryRepository;
import com.arigs.rms.repository.InterviewRepository;
import com.arigs.rms.repository.JobRequestRepository;
import com.arigs.rms.repository.JobStatusHistoryRepository;
import com.arigs.rms.repository.JoiningRepository;
import com.arigs.rms.repository.OfferRepository;
import com.arigs.rms.service.CandidateService;
import com.arigs.rms.specification.CandidateSpecification;
import com.arigs.rms.validator.WorkflowValidator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default candidate workflow service.
 */
@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateStatusHistoryRepository historyRepository;
    private final InterviewRepository interviewRepository;
    private final OfferRepository offerRepository;
    private final JoiningRepository joiningRepository;
    private final JobRequestRepository jobRequestRepository;
    private final JobStatusHistoryRepository jobStatusHistoryRepository;
    private final CandidateMapper mapper;
    private final WorkflowValidator workflowValidator;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    @Transactional
    public CandidateResponse create(CandidateCreateRequest request) {
        if (candidateRepository.existsByCandidateCodeIgnoreCase(request.candidateCode())) {
            throw new DuplicateResourceException("Candidate code already exists");
        }
        JobRequest jobRequest = jobRequestRepository.findWithAssignmentsById(request.jobRequestId())
                .filter(JobRequest::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));
        if (jobRequest.getStatus() == JobRequestStatus.TAG_ASSOCIATE_ASSIGNED) {
            transitionJob(jobRequest, JobRequestStatus.CANDIDATE_SOURCING, "Candidate sourcing started");
        } else if (jobRequest.getStatus() != JobRequestStatus.CANDIDATE_SOURCING
                && jobRequest.getStatus() != JobRequestStatus.INTERVIEW_IN_PROGRESS
                && jobRequest.getStatus() != JobRequestStatus.OFFER_RELEASED) {
            throw new BusinessException("Candidates can be uploaded only after TAG associate assignment");
        }
        Candidate candidate = new Candidate();
        candidate.setJobRequest(jobRequest);
        candidate.setCandidateCode(request.candidateCode().trim());
        candidate.setFirstName(request.firstName().trim());
        candidate.setLastName(request.lastName().trim());
        candidate.setEmail(request.email().trim().toLowerCase());
        candidate.setPhone(request.phone().trim());
        candidate.setCurrentCompany(request.currentCompany());
        candidate.setTotalExperienceYears(request.totalExperienceYears());
        candidate.setExpectedCtc(request.expectedCtc());
        candidate.setCurrentCtc(request.currentCtc());
        candidate.setNoticePeriodDays(request.noticePeriodDays());
        Candidate saved = candidateRepository.save(candidate);
        recordHistory(saved, null, saved.getStatus(), "Candidate uploaded");
        publishCandidateEvent("candidate.created", saved, null, saved.getStatus());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CandidateResponse updateStatus(UUID id, CandidateStatusUpdateRequest request) {
        Candidate candidate = load(id);
        transition(candidate, request.status(), request.comments());
        return mapper.toResponse(candidate);
    }

    @Override
    @Transactional
    public InterviewResponse scheduleInterview(UUID candidateId, InterviewRequest request) {
        Candidate candidate = load(candidateId);
        if (candidate.getStatus() == CandidateStatus.SHORTLISTED) {
            transition(candidate, CandidateStatus.INTERVIEW_SCHEDULED, "Interview scheduled");
        }
        JobRequest jobRequest = candidate.getJobRequest();
        if (jobRequest.getStatus() == JobRequestStatus.CANDIDATE_SOURCING) {
            transitionJob(jobRequest, JobRequestStatus.INTERVIEW_IN_PROGRESS, "Interview process started");
        }
        Interview interview = new Interview();
        interview.setCandidate(candidate);
        interview.setRoundName(request.roundName().trim());
        interview.setScheduledAt(request.scheduledAt());
        interview.setMode(request.mode().trim());
        interview.setInterviewerName(request.interviewerName().trim());
        interview.setFeedback(request.feedback());
        interview.setResult(request.result() == null ? InterviewResult.SCHEDULED : request.result());
        return mapper.toInterviewResponse(interviewRepository.save(interview));
    }

    @Override
    @Transactional
    public OfferResponse releaseOffer(UUID candidateId, OfferRequest request) {
        Candidate candidate = load(candidateId);
        if (candidate.getStatus() == CandidateStatus.SELECTED) {
            transition(candidate, CandidateStatus.OFFER_RELEASED, "Offer released");
        } else if (candidate.getStatus() != CandidateStatus.OFFER_RELEASED && candidate.getStatus() != CandidateStatus.OFFER_ACCEPTED) {
            throw new BusinessException("Offer can be released only after candidate selection");
        }
        JobRequest jobRequest = candidate.getJobRequest();
        if (jobRequest.getStatus() == JobRequestStatus.INTERVIEW_IN_PROGRESS) {
            transitionJob(jobRequest, JobRequestStatus.OFFER_RELEASED, "Offer released to candidate");
        }
        Offer offer = offerRepository.findByCandidateIdAndActiveTrue(candidateId).orElseGet(Offer::new);
        offer.setCandidate(candidate);
        offer.setOfferAmount(request.offerAmount());
        offer.setJoiningDate(request.joiningDate());
        offer.setStatus(request.status() == null ? OfferStatus.RELEASED : request.status());
        if (offer.getStatus() == OfferStatus.ACCEPTED && candidate.getStatus() == CandidateStatus.OFFER_RELEASED) {
            transition(candidate, CandidateStatus.OFFER_ACCEPTED, "Offer accepted");
        }
        return mapper.toOfferResponse(offerRepository.save(offer));
    }

    @Override
    @Transactional
    public JoiningResponse confirmJoining(UUID candidateId, JoiningRequest request) {
        Candidate candidate = load(candidateId);
        if (request.status() == JoiningStatus.JOINED && candidate.getStatus() == CandidateStatus.OFFER_ACCEPTED) {
            transition(candidate, CandidateStatus.JOINED, "Candidate joined");
            JobRequest jobRequest = candidate.getJobRequest();
            if (jobRequest.getStatus() == JobRequestStatus.OFFER_RELEASED) {
                transitionJob(jobRequest, JobRequestStatus.JOINED, "Candidate joined");
            }
        }
        Joining joining = joiningRepository.findByCandidateIdAndActiveTrue(candidateId).orElseGet(Joining::new);
        joining.setCandidate(candidate);
        joining.setJoiningDate(request.joiningDate());
        joining.setStatus(request.status());
        joining.setRemarks(request.remarks());
        return mapper.toJoiningResponse(joiningRepository.save(joining));
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateResponse get(UUID id) {
        return mapper.toResponse(load(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CandidateResponse> search(UUID jobRequestId, CandidateStatus status, String keyword, Pageable pageable) {
        return PageResponse.from(candidateRepository.findAll(CandidateSpecification.search(jobRequestId, status, keyword), pageable)
                .map(mapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusHistoryResponse> history(UUID id) {
        return historyRepository.findByCandidateIdOrderByCreatedDateAsc(id).stream()
                .map(mapper::toHistoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> interviews(UUID candidateId) {
        return interviewRepository.findByCandidateIdAndActiveTrueOrderByScheduledAtAsc(candidateId).stream()
                .map(mapper::toInterviewResponse)
                .toList();
    }

    private Candidate load(UUID id) {
        return candidateRepository.findWithJobRequestById(id)
                .filter(Candidate::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
    }

    private void transition(Candidate candidate, CandidateStatus nextStatus, String comments) {
        CandidateStatus oldStatus = candidate.getStatus();
        workflowValidator.validateCandidateTransition(oldStatus, nextStatus);
        candidate.setStatus(nextStatus);
        recordHistory(candidate, oldStatus, nextStatus, comments);
        publishCandidateEvent("candidate.status-changed", candidate, oldStatus, nextStatus);
    }

    private void recordHistory(Candidate candidate, CandidateStatus oldStatus, CandidateStatus newStatus, String comments) {
        CandidateStatusHistory history = new CandidateStatusHistory();
        history.setCandidate(candidate);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setComments(comments);
        historyRepository.save(history);
    }

    private void transitionJob(JobRequest jobRequest, JobRequestStatus nextStatus, String comments) {
        JobRequestStatus oldStatus = jobRequest.getStatus();
        workflowValidator.validateJobTransition(oldStatus, nextStatus);
        jobRequest.setStatus(nextStatus);
        JobStatusHistory history = new JobStatusHistory();
        history.setJobRequest(jobRequest);
        history.setOldStatus(oldStatus);
        history.setNewStatus(nextStatus);
        history.setComments(comments);
        jobStatusHistoryRepository.save(history);
        domainEventPublisher.publish(DomainEvent.of("job-request.status-changed", "JobRequest",
                jobRequest.getId().toString(), Map.of(
                        "requestNumber", jobRequest.getRequestNumber(),
                        "oldStatus", oldStatus == null ? "" : oldStatus.name(),
                        "newStatus", nextStatus.name())));
    }

    private void publishCandidateEvent(String eventType, Candidate candidate, CandidateStatus oldStatus, CandidateStatus newStatus) {
        domainEventPublisher.publish(DomainEvent.of(eventType, "Candidate", candidate.getId().toString(), Map.of(
                "candidateCode", candidate.getCandidateCode(),
                "jobRequestId", candidate.getJobRequest().getId().toString(),
                "oldStatus", oldStatus == null ? "" : oldStatus.name(),
                "newStatus", newStatus.name())));
    }
}
