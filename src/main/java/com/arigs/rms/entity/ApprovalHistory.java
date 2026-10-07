package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Approval decision audit for workflow-driven business objects.
 */
@Getter
@Setter
@Entity
@Table(name = "approval_history", indexes = {
        @Index(name = "idx_approval_history_entity", columnList = "entity_type,entity_id"),
        @Index(name = "idx_approval_history_action", columnList = "workflow_action_id")
})
public class ApprovalHistory extends BaseAuditableEntity {

    @Column(name = "entity_type", nullable = false, length = 80)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_action_id", nullable = false)
    private WorkflowAction workflowAction;

    @Column(nullable = false, length = 30)
    private String decision;

    @Column(columnDefinition = "text")
    private String comments;
}
