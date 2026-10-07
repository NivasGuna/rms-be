package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.AssignmentRequest;
import com.arigs.rms.dto.request.DecisionRequest;
import com.arigs.rms.dto.request.JobRequestCreateRequest;
import com.arigs.rms.dto.request.StatusUpdateRequest;
import com.arigs.rms.dto.response.JobRequestResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.service.JobRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Job request workflow API.
 */
@RestController
@RequestMapping("/api/v1/job-requests")
@RequiredArgsConstructor
@Tag(name = "Job Requests")
public class JobRequestController {

    private final JobRequestService jobRequestService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM')")
    @Operation(summary = "Create a job request")
    public ResponseEntity<ApiResponse<JobRequestResponse>> create(@Valid @RequestBody JobRequestCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Job request created", jobRequestService.create(request)));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM')")
    @Operation(summary = "Approve a job request")
    public ResponseEntity<ApiResponse<JobRequestResponse>> approve(
            @PathVariable UUID id,
            @Valid @RequestBody DecisionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job request approved", jobRequestService.approve(id, request)));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM')")
    @Operation(summary = "Reject a job request")
    public ResponseEntity<ApiResponse<JobRequestResponse>> reject(
            @PathVariable UUID id,
            @Valid @RequestBody DecisionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job request rejected", jobRequestService.reject(id, request)));
    }

    @PostMapping("/{id}/tag-manager")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM')")
    @Operation(summary = "Assign TAG manager")
    public ResponseEntity<ApiResponse<JobRequestResponse>> assignTagManager(
            @PathVariable UUID id,
            @Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "TAG manager assigned", jobRequestService.assignTagManager(id, request)));
    }

    @PostMapping("/{id}/tag-associate")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER')")
    @Operation(summary = "Assign TAG associate")
    public ResponseEntity<ApiResponse<JobRequestResponse>> assignTagAssociate(
            @PathVariable UUID id,
            @Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "TAG associate assigned", jobRequestService.assignTagAssociate(id, request)));
    }

    @PostMapping("/{id}/start-sourcing")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Start candidate sourcing")
    public ResponseEntity<ApiResponse<JobRequestResponse>> startSourcing(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Candidate sourcing started", jobRequestService.startSourcing(id, request)));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM')")
    @Operation(summary = "Submit a job request for business approval")
    public ResponseEntity<ApiResponse<JobRequestResponse>> submit(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job request submitted for approval", jobRequestService.submit(id)));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER')")
    @Operation(summary = "Close a fulfilled job request")
    public ResponseEntity<ApiResponse<JobRequestResponse>> close(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job request closed", jobRequestService.close(id, request)));
    }

    @GetMapping("/approvals")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM')")
    @Operation(summary = "Get job requests pending approval")
    public ResponseEntity<ApiResponse<PageResponse<JobRequestResponse>>> approvals(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Pending approval requests retrieved",
                jobRequestService.getApprovals(keyword, pageable)));
    }

    @GetMapping("/assignments")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Get job requests pending assignments")
    public ResponseEntity<ApiResponse<PageResponse<JobRequestResponse>>> assignments(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Assignment requests retrieved",
                jobRequestService.getAssignments(keyword, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get a job request")
    public ResponseEntity<ApiResponse<JobRequestResponse>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job request retrieved", jobRequestService.get(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Search job requests")
    public ResponseEntity<ApiResponse<PageResponse<JobRequestResponse>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobRequestStatus status,
            @RequestParam(required = false) List<JobRequestStatus> statuses,
            Pageable pageable) {
        if (statuses != null && !statuses.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job requests retrieved",
                    jobRequestService.search(keyword, statuses, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job requests retrieved",
                jobRequestService.search(keyword, status, pageable)));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get job request status history")
    public ResponseEntity<ApiResponse<List<StatusHistoryResponse>>> history(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Job request history retrieved", jobRequestService.history(id)));
    }
}
