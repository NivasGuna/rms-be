package com.arigs.rms.repository;

import com.arigs.rms.entity.WorkflowAction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;

/**
 * Repository for workflow actions.
 */
public interface WorkflowActionRepository extends BaseMasterRepository<WorkflowAction> {

    @EntityGraph(attributePaths = {"workflowDefinition", "fromStep", "toStep"})
    Optional<WorkflowAction> findByWorkflowDefinitionIdAndCodeIgnoreCaseAndFromStepCodeIgnoreCaseAndActiveTrueAndDeletedFalse(
            UUID workflowDefinitionId,
            String code,
            String fromStepCode);
}
