package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.AssignmentRequest;
import com.arigs.rms.dto.request.DecisionRequest;
import com.arigs.rms.dto.request.JobRequestCreateRequest;
import com.arigs.rms.dto.request.StatusUpdateRequest;
import com.arigs.rms.dto.response.JobRequestResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.JobRequestStatus;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Job request workflow use cases.
 */
public interface JobRequestService {

    JobRequestResponse create(JobRequestCreateRequest request);

    JobRequestResponse submit(UUID id);

    JobRequestResponse approve(UUID id, DecisionRequest request);

    JobRequestResponse reject(UUID id, DecisionRequest request);

    JobRequestResponse assignTagManager(UUID id, AssignmentRequest request);

    JobRequestResponse assignTagAssociate(UUID id, AssignmentRequest request);

    JobRequestResponse startSourcing(UUID id, StatusUpdateRequest request);

    JobRequestResponse close(UUID id, StatusUpdateRequest request);

    JobRequestResponse get(UUID id);

    PageResponse<JobRequestResponse> search(String keyword, JobRequestStatus status, Pageable pageable);

    PageResponse<JobRequestResponse> search(String keyword, Collection<JobRequestStatus> statuses, Pageable pageable);

    PageResponse<JobRequestResponse> getApprovals(String keyword, Pageable pageable);

    PageResponse<JobRequestResponse> getAssignments(String keyword, Pageable pageable);

    List<StatusHistoryResponse> history(UUID id);
}
