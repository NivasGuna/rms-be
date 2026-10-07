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
 * Immutable audit trail of candidate status changes.
 */
@Getter
@Setter
@Entity
@Table(name = "candidate_status_history", indexes = {
        @Index(name = "idx_candidate_status_history_candidate", columnList = "candidate_id"),
        @Index(name = "idx_candidate_status_history_new_status", columnList = "new_status")
})
public class CandidateStatusHistory extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 40)
    private CandidateStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 40)
    private CandidateStatus newStatus;

    @Column(columnDefinition = "text")
    private String comments;
}
