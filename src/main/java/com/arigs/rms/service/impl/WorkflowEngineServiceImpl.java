package com.arigs.rms.service.impl;

import com.arigs.rms.audit.AuditEventType;
import com.arigs.rms.audit.AuditService;
import com.arigs.rms.dto.request.BusinessRuleEvaluationRequest;
import com.arigs.rms.dto.request.WorkflowTransitionRequest;
import com.arigs.rms.dto.response.BusinessRuleEvaluationResponse;
import com.arigs.rms.dto.response.WorkflowTransitionResponse;
import com.arigs.rms.entity.ApprovalHistory;
import com.arigs.rms.entity.WorkflowAction;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.repository.ApprovalHistoryRepository;
import com.arigs.rms.repository.WorkflowActionRepository;
import com.arigs.rms.repository.WorkflowDefinitionRepository;
import com.arigs.rms.security.PermissionService;
import com.arigs.rms.service.BusinessRuleService;
import com.arigs.rms.service.WorkflowEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default data-driven workflow engine.
 */
@Service
@RequiredArgsConstructor
public class WorkflowEngineServiceImpl implements WorkflowEngineService {

    private final WorkflowDefinitionRepository workflowDefinitionRepository;
    private final WorkflowActionRepository workflowActionRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final PermissionService permissionService;
    private final BusinessRuleService businessRuleService;
    private final AuditService auditService;

    @Override
    @Transactional
    public WorkflowTransitionResponse transition(WorkflowTransitionRequest request) {
        var definition = workflowDefinitionRepository
                .findByEntityTypeAndDefaultWorkflowTrueAndActiveTrueAndDeletedFalse(request.entityType())
                .orElseThrow(() -> new ResourceNotFoundException("Workflow definition not found for " + request.entityType()));
        WorkflowAction action = workflowActionRepository
                .findByWorkflowDefinitionIdAndCodeIgnoreCaseAndFromStepCodeIgnoreCaseAndActiveTrueAndDeletedFalse(
                        definition.getId(), request.actionCode(), request.currentStepCode())
                .orElseThrow(() -> new BusinessException("Workflow transition is not configured"));
        if (action.isRequiresComment() && !StringUtils.hasText(request.comments())) {
            throw new BusinessException("Comment is required for workflow action " + action.getCode());
        }
        if (StringUtils.hasText(action.getRequiredPermission())
                && !permissionService.currentUserHasPermission(action.getRequiredPermission())) {
            throw new BusinessException("Permission denied for workflow action " + action.getCode());
        }
        if (request.facts() != null && !request.facts().isEmpty()) {
            BusinessRuleEvaluationResponse evaluation = businessRuleService.evaluate(
                    new BusinessRuleEvaluationRequest(request.entityType() + ":" + action.getCode(), request.facts()));
            if (!evaluation.passed()) {
                throw new BusinessException(String.join("; ", evaluation.violations()));
            }
        }

        ApprovalHistory history = new ApprovalHistory();
        history.setEntityType(request.entityType());
        history.setEntityId(request.entityId());
        history.setWorkflowAction(action);
        history.setDecision(action.getCode());
        history.setComments(request.comments());
        approvalHistoryRepository.save(history);

        auditService.record(AuditEventType.APPROVAL, action.getCode(), request.entityType(),
                request.entityId().toString(), "Workflow transitioned to " + action.getToStep().getCode());

        return new WorkflowTransitionResponse(
                action.getId(),
                action.getFromStep().getCode(),
                action.getToStep().getCode(),
                action.getToStep().getName(),
                true);
    }
}
