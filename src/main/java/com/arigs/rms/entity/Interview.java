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
 * Candidate interview schedule and feedback.
 */
@Getter
@Setter
@Entity
@Table(name = "interviews", indexes = {
        @Index(name = "idx_interviews_candidate", columnList = "candidate_id"),
        @Index(name = "idx_interviews_scheduled_at", columnList = "scheduled_at"),
        @Index(name = "idx_interviews_result", columnList = "result")
})
public class Interview extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "round_name", nullable = false, length = 80)
    private String roundName;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(nullable = false, length = 40)
    private String mode;

    @Column(name = "interviewer_name", nullable = false, length = 150)
    private String interviewerName;

    @Column(columnDefinition = "text")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewResult result = InterviewResult.SCHEDULED;
}
