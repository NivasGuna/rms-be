package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.dto.response.DashboardKpiResponse;
import com.arigs.rms.dto.response.MonthlyHiringResponse;
import com.arigs.rms.dto.response.RecruiterPerformanceResponse;
import com.arigs.rms.dto.response.StatusCountResponse;
import com.arigs.rms.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Optimized dashboard API.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get dashboard KPI summary")
    public ResponseEntity<ApiResponse<DashboardKpiResponse>> kpis() {
        return ApiResponseBuilder.ok("Dashboard KPIs retrieved", dashboardService.kpis());
    }

    @GetMapping("/candidate-pipeline")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Get candidate pipeline counts")
    public ResponseEntity<ApiResponse<List<StatusCountResponse>>> candidatePipeline() {
        return ApiResponseBuilder.ok("Candidate pipeline retrieved", dashboardService.candidatePipeline());
    }

    @GetMapping("/interview-pipeline")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Get interview pipeline counts")
    public ResponseEntity<ApiResponse<List<StatusCountResponse>>> interviewPipeline() {
        return ApiResponseBuilder.ok("Interview pipeline retrieved", dashboardService.interviewPipeline());
    }

    @GetMapping("/offer-pipeline")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Get offer pipeline counts")
    public ResponseEntity<ApiResponse<List<StatusCountResponse>>> offerPipeline() {
        return ApiResponseBuilder.ok("Offer pipeline retrieved", dashboardService.offerPipeline());
    }

    @GetMapping("/joining-statistics")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Get joining statistics")
    public ResponseEntity<ApiResponse<List<StatusCountResponse>>> joiningStatistics() {
        return ApiResponseBuilder.ok("Joining statistics retrieved", dashboardService.joiningStatistics());
    }

    @GetMapping("/monthly-hiring")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Get monthly hiring trend")
    public ResponseEntity<ApiResponse<List<MonthlyHiringResponse>>> monthlyHiring(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponseBuilder.ok("Monthly hiring retrieved", dashboardService.monthlyHiring(from, to));
    }

    @GetMapping("/recruiter-performance")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','READ_ONLY_USER')")
    @Operation(summary = "Get recruiter performance")
    public ResponseEntity<ApiResponse<List<RecruiterPerformanceResponse>>> recruiterPerformance() {
        return ApiResponseBuilder.ok("Recruiter performance retrieved", dashboardService.recruiterPerformance());
    }
}
