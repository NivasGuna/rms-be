package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
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
import com.arigs.rms.entity.CandidateStatus;
import com.arigs.rms.service.CandidateService;
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
 * Candidate workflow API.
 */
@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Upload candidate metadata")
    public ResponseEntity<ApiResponse<CandidateResponse>> create(@Valid @RequestBody CandidateCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Candidate created", candidateService.create(request)));
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Update candidate status")
    public ResponseEntity<ApiResponse<CandidateResponse>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody CandidateStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Candidate status updated", candidateService.updateStatus(id, request)));
    }

    @PostMapping("/{id}/interviews")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Schedule candidate interview")
    public ResponseEntity<ApiResponse<InterviewResponse>> scheduleInterview(
            @PathVariable UUID id,
            @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Interview scheduled", candidateService.scheduleInterview(id, request)));
    }

    @PostMapping("/{id}/offer")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER')")
    @Operation(summary = "Release candidate offer")
    public ResponseEntity<ApiResponse<OfferResponse>> releaseOffer(
            @PathVariable UUID id,
            @Valid @RequestBody OfferRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Offer saved", candidateService.releaseOffer(id, request)));
    }

    @PostMapping("/{id}/joining")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER')")
    @Operation(summary = "Confirm candidate joining")
    public ResponseEntity<ApiResponse<JoiningResponse>> confirmJoining(
            @PathVariable UUID id,
            @Valid @RequestBody JoiningRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Joining saved", candidateService.confirmJoining(id, request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get candidate")
    public ResponseEntity<ApiResponse<CandidateResponse>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Candidate retrieved", candidateService.get(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Search candidates")
    public ResponseEntity<ApiResponse<PageResponse<CandidateResponse>>> search(
            @RequestParam(required = false) UUID jobRequestId,
            @RequestParam(required = false) CandidateStatus status,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Candidates retrieved",
                candidateService.search(jobRequestId, status, keyword, pageable)));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get candidate status history")
    public ResponseEntity<ApiResponse<List<StatusHistoryResponse>>> history(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Candidate history retrieved", candidateService.history(id)));
    }

    @GetMapping("/{id}/interviews")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "List candidate interviews")
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> interviews(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Candidate interviews retrieved", candidateService.interviews(id)));
    }
}
