package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.response.BackgroundJobExecutionResponse;
import com.arigs.rms.service.BackgroundJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Background job administration API.
 */
@RestController
@RequestMapping("/api/v1/background-jobs")
@RequiredArgsConstructor
@Tag(name = "Background Jobs")
public class BackgroundJobController {

    private final BackgroundJobService backgroundJobService;

    @PostMapping("/{jobCode}/run")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Run a configured background job")
    public ResponseEntity<ApiResponse<BackgroundJobExecutionResponse>> run(@PathVariable String jobCode) {
        return ApiResponseBuilder.accepted("Background job executed", backgroundJobService.run(jobCode));
    }

    @GetMapping("/executions")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "List background job executions")
    public ResponseEntity<ApiResponse<PageResponse<BackgroundJobExecutionResponse>>> executions(Pageable pageable) {
        return ApiResponseBuilder.ok("Background job executions retrieved", backgroundJobService.executions(pageable));
    }
}
