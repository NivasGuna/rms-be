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
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Sales-originated job request moving through the RMS workflow.
 */
@Getter
@Setter
@Entity
@Table(name = "job_requests", indexes = {
        @Index(name = "idx_job_requests_status", columnList = "status"),
        @Index(name = "idx_job_requests_sales_owner", columnList = "sales_owner_id"),
        @Index(name = "idx_job_requests_business_approver", columnList = "business_approver_id"),
        @Index(name = "idx_job_requests_tag_manager", columnList = "tag_manager_id"),
        @Index(name = "idx_job_requests_tag_associate", columnList = "tag_associate_id"),
        @Index(name = "idx_job_requests_target_date", columnList = "target_date")
})
public class JobRequest extends BaseAuditableEntity {

    @Column(name = "request_number", nullable = false, unique = true, length = 40)
    private String requestNumber;

    @Column(name = "client_name", nullable = false, length = 150)
    private String clientName;

    @Column(name = "project_name", nullable = false, length = 150)
    private String projectName;

    @Column(name = "job_title", nullable = false, length = 150)
    private String jobTitle;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 120)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 30)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority;

    @Column(name = "number_of_positions", nullable = false)
    private int numberOfPositions;

    @Column(name = "min_experience_years", nullable = false)
    private int minExperienceYears;

    @Column(name = "max_experience_years", nullable = false)
    private int maxExperienceYears;

    @Column(name = "budget_amount", precision = 14, scale = 2)
    private BigDecimal budgetAmount;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private JobRequestStatus status = JobRequestStatus.PENDING_BUSINESS_APPROVAL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sales_owner_id", nullable = false)
    private AppUser salesOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_approver_id")
    private AppUser businessApprover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_manager_id")
    private AppUser tagManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_associate_id")
    private AppUser tagAssociate;
}
