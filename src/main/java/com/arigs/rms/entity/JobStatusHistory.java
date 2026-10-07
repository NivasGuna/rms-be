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
 * Immutable audit trail of job request status changes.
 */
@Getter
@Setter
@Entity
@Table(name = "job_status_history", indexes = {
        @Index(name = "idx_job_status_history_job", columnList = "job_request_id"),
        @Index(name = "idx_job_status_history_new_status", columnList = "new_status")
})
public class JobStatusHistory extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 40)
    private JobRequestStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 40)
    private JobRequestStatus newStatus;

    @Column(columnDefinition = "text")
    private String comments;
}
