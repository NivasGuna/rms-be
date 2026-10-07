package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.AuditSearchRequest;
import com.arigs.rms.dto.response.AuditTrailResponse;
import com.arigs.rms.service.AuditTrailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Audit trail query API.
 */
@RestController
@RequestMapping("/api/v1/audit-trails")
@RequiredArgsConstructor
@Tag(name = "Audit Trails")
public class AuditTrailController {

    private final AuditTrailService auditTrailService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Search audit trail events")
    public ResponseEntity<ApiResponse<PageResponse<AuditTrailResponse>>> search(@Valid @RequestBody AuditSearchRequest request) {
        return ApiResponseBuilder.ok("Audit trails retrieved", auditTrailService.search(request));
    }
}
