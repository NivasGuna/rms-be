package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Offer status master.
 */
@Getter
@Setter
@Entity
@Table(name = "offer_statuses", indexes = {
        @Index(name = "idx_offer_statuses_code", columnList = "code")
})
public class OfferStatusMaster extends BaseMasterEntity {

    @Column(name = "terminal_status", nullable = false)
    private boolean terminalStatus;
}
