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
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Execution history for a background job.
 */
@Getter
@Setter
@Entity
@Table(name = "background_job_executions", indexes = {
        @Index(name = "idx_background_job_executions_definition", columnList = "job_definition_id"),
        @Index(name = "idx_background_job_executions_status", columnList = "status")
})
public class BackgroundJobExecution extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_definition_id", nullable = false)
    private BackgroundJobDefinition jobDefinition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BackgroundJobStatus status = BackgroundJobStatus.SCHEDULED;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "failure_reason", columnDefinition = "text")
    private String failureReason;
}
