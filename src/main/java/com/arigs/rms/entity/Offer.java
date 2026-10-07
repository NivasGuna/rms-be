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
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Offer released to a selected candidate.
 */
@Getter
@Setter
@Entity
@Table(name = "offers", indexes = {
        @Index(name = "idx_offers_candidate", columnList = "candidate_id"),
        @Index(name = "idx_offers_status", columnList = "status")
})
public class Offer extends BaseAuditableEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false, unique = true)
    private Candidate candidate;

    @Column(name = "offer_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal offerAmount;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OfferStatus status = OfferStatus.RELEASED;
}
