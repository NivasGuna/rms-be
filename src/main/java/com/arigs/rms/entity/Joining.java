package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Candidate joining confirmation.
 */
@Getter
@Setter
@Entity
@Table(name = "joinings", indexes = {
        @Index(name = "idx_joinings_candidate", columnList = "candidate_id"),
        @Index(name = "idx_joinings_status", columnList = "status")
})
public class Joining extends BaseAuditableEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false, unique = true)
    private Candidate candidate;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JoiningStatus status;

    @Column(columnDefinition = "text")
    private String remarks;
}
