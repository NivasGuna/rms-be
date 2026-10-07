package com.arigs.rms.service.impl;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.AssignmentRequest;
import com.arigs.rms.dto.request.DecisionRequest;
import com.arigs.rms.dto.request.JobRequestCreateRequest;
import com.arigs.rms.dto.request.StatusUpdateRequest;
import com.arigs.rms.dto.response.JobRequestResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.AppUser;
import com.arigs.rms.entity.JobRequest;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.entity.JobStatusHistory;
import com.arigs.rms.entity.Role;
import com.arigs.rms.event.DomainEvent;
import com.arigs.rms.event.DomainEventPublisher;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.exception.DuplicateResourceException;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.mapper.JobRequestMapper;
import com.arigs.rms.repository.AppUserRepository;
import com.arigs.rms.repository.JobRequestRepository;
import com.arigs.rms.repository.JobStatusHistoryRepository;
import com.arigs.rms.security.UserPrincipal;
import com.arigs.rms.service.JobRequestService;
import com.arigs.rms.specification.JobRequestSpecification;
import com.arigs.rms.validator.WorkflowValidator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default job request workflow service.
 */
@Service
@RequiredArgsConstructor
public class JobRequestServiceImpl implements JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final JobStatusHistoryRepository historyRepository;
    private final AppUserRepository userRepository;
    private final JobRequestMapper mapper;
    private final WorkflowValidator workflowValidator;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    @Transactional
    public JobRequestResponse create(JobRequestCreateRequest request) {
        if (request.maxExperienceYears() < request.minExperienceYears()) {
            throw new BusinessException("Maximum experience must be greater than or equal to minimum experience");
        }
        if (jobRequestRepository.existsByRequestNumberIgnoreCase(request.requestNumber())) {
            throw new DuplicateResourceException("Job request number already exists");
        }
        JobRequest jobRequest = new JobRequest();
        jobRequest.setRequestNumber(request.requestNumber().trim());
        jobRequest.setClientName(request.clientName().trim());
        jobRequest.setProjectName(request.projectName().trim());
        jobRequest.setJobTitle(request.jobTitle().trim());
        jobRequest.setDescription(request.description().trim());
        jobRequest.setLocation(request.location().trim());
        jobRequest.setEmploymentType(request.employmentType());
        jobRequest.setPriority(request.priority());
        jobRequest.setNumberOfPositions(request.numberOfPositions());
        jobRequest.setMinExperienceYears(request.minExperienceYears());
        jobRequest.setMaxExperienceYears(request.maxExperienceYears());
        jobRequest.setBudgetAmount(request.budgetAmount());
        jobRequest.setTargetDate(request.targetDate());
        jobRequest.setSalesOwner(loadUserWithRole(request.salesOwnerId(), Role.SALES_TEAM));
        JobRequest saved = jobRequestRepository.save(jobRequest);
        recordHistory(saved, null, saved.getStatus(), "Job request submitted for business approval");
        publishJobEvent("job-request.created", saved, null, saved.getStatus());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public JobRequestResponse submit(UUID id) {
        JobRequest jobRequest = load(id);
        transition(jobRequest, JobRequestStatus.PENDING_BUSINESS_APPROVAL, "Submitted for business approval");
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional
    public JobRequestResponse approve(UUID id, DecisionRequest request) {
        JobRequest jobRequest = load(id);
        transition(jobRequest, JobRequestStatus.BUSINESS_APPROVED, request.comments());
        jobRequest.setBusinessApprover(currentApprover());
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional
    public JobRequestResponse reject(UUID id, DecisionRequest request) {
        JobRequest jobRequest = load(id);
        transition(jobRequest, JobRequestStatus.BUSINESS_REJECTED, request.comments());
        jobRequest.setBusinessApprover(currentApprover());
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional
    public JobRequestResponse assignTagManager(UUID id, AssignmentRequest request) {
        JobRequest jobRequest = load(id);
        jobRequest.setTagManager(loadUserWithRole(request.userId(), Role.TAG_MANAGER));
        transition(jobRequest, JobRequestStatus.TAG_MANAGER_ASSIGNED, request.comments());
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional
    public JobRequestResponse assignTagAssociate(UUID id, AssignmentRequest request) {
        JobRequest jobRequest = load(id);
        if (jobRequest.getTagManager() == null) {
            throw new BusinessException("TAG manager must be assigned before TAG associate");
        }
        jobRequest.setTagAssociate(loadUserWithRole(request.userId(), Role.TAG_ASSOCIATE));
        transition(jobRequest, JobRequestStatus.TAG_ASSOCIATE_ASSIGNED, request.comments());
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional
    public JobRequestResponse startSourcing(UUID id, StatusUpdateRequest request) {
        JobRequest jobRequest = load(id);
        transition(jobRequest, JobRequestStatus.CANDIDATE_SOURCING, request.comments());
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional
    public JobRequestResponse close(UUID id, StatusUpdateRequest request) {
        JobRequest jobRequest = load(id);
        transition(jobRequest, JobRequestStatus.CLOSED, request.comments());
        return mapper.toResponse(jobRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public JobRequestResponse get(UUID id) {
        return mapper.toResponse(load(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobRequestResponse> search(String keyword, JobRequestStatus status, Pageable pageable) {
        return search(keyword, status != null ? List.of(status) : null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobRequestResponse> search(String keyword, java.util.Collection<JobRequestStatus> statuses, Pageable pageable) {
        return PageResponse.from(jobRequestRepository.findAll(JobRequestSpecification.search(keyword, statuses), pageable)
                .map(mapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobRequestResponse> getApprovals(String keyword, Pageable pageable) {
        return search(keyword, List.of(JobRequestStatus.PENDING_BUSINESS_APPROVAL, JobRequestStatus.BUSINESS_APPROVED), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobRequestResponse> getAssignments(String keyword, Pageable pageable) {
        return search(keyword, List.of(
                JobRequestStatus.BUSINESS_APPROVED,
                JobRequestStatus.TAG_MANAGER_ASSIGNED,
                JobRequestStatus.TAG_ASSOCIATE_ASSIGNED,
                JobRequestStatus.CANDIDATE_SOURCING
        ), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusHistoryResponse> history(UUID id) {
        return historyRepository.findByJobRequestIdOrderByCreatedDateAsc(id).stream()
                .map(mapper::toHistoryResponse)
                .toList();
    }

    private JobRequest load(UUID id) {
        return jobRequestRepository.findWithAssignmentsById(id)
                .filter(JobRequest::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));
    }

    private AppUser loadUserWithRole(UUID userId, Role requiredRole) {
        AppUser user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getRoles().contains(requiredRole) && !user.getRoles().contains(Role.ADMINISTRATOR)) {
            throw new BusinessException("User must have role " + requiredRole);
        }
        return user;
    }

    private AppUser currentApprover() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new BusinessException("Authenticated approver is required");
        }
        AppUser user = userRepository.findByIdAndActiveTrue(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getRoles().contains(Role.BUSINESS_TEAM) && !user.getRoles().contains(Role.ADMINISTRATOR)) {
            throw new BusinessException("User must have role BUSINESS_TEAM or ADMINISTRATOR");
        }
        return user;
    }

    private void transition(JobRequest jobRequest, JobRequestStatus nextStatus, String comments) {
        JobRequestStatus oldStatus = jobRequest.getStatus();
        workflowValidator.validateJobTransition(oldStatus, nextStatus);
        jobRequest.setStatus(nextStatus);
        recordHistory(jobRequest, oldStatus, nextStatus, comments);
        publishJobEvent("job-request.status-changed", jobRequest, oldStatus, nextStatus);
    }

    private void recordHistory(JobRequest jobRequest, JobRequestStatus oldStatus, JobRequestStatus newStatus, String comments) {
        JobStatusHistory history = new JobStatusHistory();
        history.setJobRequest(jobRequest);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setComments(comments);
        historyRepository.save(history);
    }

    private void publishJobEvent(String eventType, JobRequest jobRequest, JobRequestStatus oldStatus, JobRequestStatus newStatus) {
        domainEventPublisher.publish(DomainEvent.of(eventType, "JobRequest", jobRequest.getId().toString(), Map.of(
                "requestNumber", jobRequest.getRequestNumber(),
                "oldStatus", oldStatus == null ? "" : oldStatus.name(),
                "newStatus", newStatus.name())));
    }
}
