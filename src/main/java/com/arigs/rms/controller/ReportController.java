package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.dto.request.ReportRequest;
import com.arigs.rms.dto.response.ReportExportResponse;
import com.arigs.rms.service.ReportService;
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
 * Reporting API.
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Export a configured report")
    public ResponseEntity<ApiResponse<ReportExportResponse>> export(@Valid @RequestBody ReportRequest request) {
        return ApiResponseBuilder.ok("Report exported", reportService.export(request));
    }
}
