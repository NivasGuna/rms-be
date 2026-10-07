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
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * Candidate submitted against an approved job request.
 */
@Getter
@Setter
@Entity
@Table(name = "candidates", indexes = {
        @Index(name = "idx_candidates_job", columnList = "job_request_id"),
        @Index(name = "idx_candidates_status", columnList = "status"),
        @Index(name = "idx_candidates_email", columnList = "email"),
        @Index(name = "idx_candidates_phone", columnList = "phone")
})
public class Candidate extends BaseAuditableEntity {

    @Column(name = "candidate_code", nullable = false, unique = true, length = 40)
    private String candidateCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(name = "current_company", length = 150)
    private String currentCompany;

    @Column(name = "total_experience_years", nullable = false)
    private int totalExperienceYears;

    @Column(name = "expected_ctc", precision = 14, scale = 2)
    private BigDecimal expectedCtc;

    @Column(name = "current_ctc", precision = 14, scale = 2)
    private BigDecimal currentCtc;

    @Column(name = "notice_period_days")
    private Integer noticePeriodDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private CandidateStatus status = CandidateStatus.UPLOADED;
}
