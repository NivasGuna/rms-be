package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.dto.response.WorkflowTimelineResponse;
import com.arigs.rms.service.WorkflowTimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Workflow timeline API.
 */
@RestController
@RequestMapping("/api/v1/timelines")
@RequiredArgsConstructor
@Tag(name = "Workflow Timelines")
public class WorkflowTimelineController {

    private final WorkflowTimelineService workflowTimelineService;

    @GetMapping("/jobs/{jobRequestId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get full job request timeline")
    public ResponseEntity<ApiResponse<List<WorkflowTimelineResponse>>> jobTimeline(@PathVariable UUID jobRequestId) {
        return ApiResponseBuilder.ok("Job timeline retrieved", workflowTimelineService.jobTimeline(jobRequestId));
    }

    @GetMapping("/candidates/{candidateId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get candidate timeline")
    public ResponseEntity<ApiResponse<List<WorkflowTimelineResponse>>> candidateTimeline(@PathVariable UUID candidateId) {
        return ApiResponseBuilder.ok("Candidate timeline retrieved", workflowTimelineService.candidateTimeline(candidateId));
    }
}
