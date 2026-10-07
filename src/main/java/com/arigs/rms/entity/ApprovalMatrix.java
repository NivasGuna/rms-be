package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Approval routing rule for a workflow step.
 */
@Getter
@Setter
@Entity
@Table(name = "approval_matrix", indexes = {
        @Index(name = "idx_approval_matrix_workflow_step", columnList = "workflow_step_id"),
        @Index(name = "idx_approval_matrix_role", columnList = "approver_role")
})
public class ApprovalMatrix extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_step_id", nullable = false)
    private WorkflowStep workflowStep;

    @Enumerated(EnumType.STRING)
    @Column(name = "approver_role", nullable = false, length = 40)
    private Role approverRole;

    @Column(name = "approval_level", nullable = false)
    private int approvalLevel;

    @Column(name = "mandatory", nullable = false)
    private boolean mandatory = true;
}
