package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.dto.request.BusinessRuleEvaluationRequest;
import com.arigs.rms.dto.request.WorkflowTransitionRequest;
import com.arigs.rms.dto.response.BusinessRuleEvaluationResponse;
import com.arigs.rms.dto.response.WorkflowTransitionResponse;
import com.arigs.rms.service.BusinessRuleService;
import com.arigs.rms.service.WorkflowEngineService;
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
 * Configurable workflow and rules API.
 */
@RestController
@RequestMapping("/api/v1/workflow-engine")
@RequiredArgsConstructor
@Tag(name = "Workflow Engine")
public class WorkflowEngineController {

    private final WorkflowEngineService workflowEngineService;
    private final BusinessRuleService businessRuleService;

    @PostMapping("/transition")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Evaluate and record a configured workflow transition")
    public ResponseEntity<ApiResponse<WorkflowTransitionResponse>> transition(@Valid @RequestBody WorkflowTransitionRequest request) {
        return ApiResponseBuilder.ok("Workflow transition completed", workflowEngineService.transition(request));
    }

    @PostMapping("/rules/evaluate")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','BUSINESS_TEAM','TAG_MANAGER')")
    @Operation(summary = "Evaluate configured business rules")
    public ResponseEntity<ApiResponse<BusinessRuleEvaluationResponse>> evaluateRules(@Valid @RequestBody BusinessRuleEvaluationRequest request) {
        return ApiResponseBuilder.ok("Business rules evaluated", businessRuleService.evaluate(request));
    }
}
