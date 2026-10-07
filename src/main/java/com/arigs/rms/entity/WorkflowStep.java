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
 * State within a configurable workflow.
 */
@Getter
@Setter
@Entity
@Table(name = "workflow_steps", indexes = {
        @Index(name = "idx_workflow_steps_definition", columnList = "workflow_definition_id"),
        @Index(name = "idx_workflow_steps_code", columnList = "code")
})
public class WorkflowStep extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_definition_id", nullable = false)
    private WorkflowDefinition workflowDefinition;

    @Column(name = "initial_step", nullable = false)
    private boolean initialStep;

    @Column(name = "terminal_step", nullable = false)
    private boolean terminalStep;
}
