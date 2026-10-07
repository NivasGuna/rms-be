package com.arigs.rms.repository;

import com.arigs.rms.entity.WorkflowStep;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for workflow steps.
 */
public interface WorkflowStepRepository extends BaseMasterRepository<WorkflowStep> {

    Optional<WorkflowStep> findByWorkflowDefinitionIdAndCodeIgnoreCaseAndActiveTrueAndDeletedFalse(UUID workflowDefinitionId, String code);
}
