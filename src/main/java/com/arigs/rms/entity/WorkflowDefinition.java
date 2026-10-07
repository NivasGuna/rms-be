package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Configurable workflow definition for an RMS business process.
 */
@Getter
@Setter
@Entity
@Table(name = "workflow_definitions", indexes = {
        @Index(name = "idx_workflow_definitions_code", columnList = "code"),
        @Index(name = "idx_workflow_definitions_entity_type", columnList = "entity_type")
})
public class WorkflowDefinition extends BaseMasterEntity {

    @Column(name = "entity_type", nullable = false, length = 80)
    private String entityType;

    @Column(name = "default_workflow", nullable = false)
    private boolean defaultWorkflow;
}
