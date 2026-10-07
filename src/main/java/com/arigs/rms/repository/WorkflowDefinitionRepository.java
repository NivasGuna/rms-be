package com.arigs.rms.repository;

import com.arigs.rms.entity.WorkflowDefinition;
import java.util.Optional;

/**
 * Repository for workflow definitions.
 */
public interface WorkflowDefinitionRepository extends BaseMasterRepository<WorkflowDefinition> {

    Optional<WorkflowDefinition> findByEntityTypeAndDefaultWorkflowTrueAndActiveTrueAndDeletedFalse(String entityType);
}
