package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Allowed transition between workflow steps.
 */
@Getter
@Setter
@Entity
@Table(name = "workflow_actions", indexes = {
        @Index(name = "idx_workflow_actions_definition", columnList = "workflow_definition_id"),
        @Index(name = "idx_workflow_actions_from_to", columnList = "from_step_id,to_step_id"),
        @Index(name = "idx_workflow_actions_code", columnList = "code")
})
public class WorkflowAction extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_definition_id", nullable = false)
    private WorkflowDefinition workflowDefinition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_step_id", nullable = false)
    private WorkflowStep fromStep;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_step_id", nullable = false)
    private WorkflowStep toStep;

    @Column(name = "required_permission", length = 80)
    private String requiredPermission;

    @Column(name = "requires_comment", nullable = false)
    private boolean requiresComment;
}
