package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Configurable candidate status master.
 */
@Getter
@Setter
@Entity
@Table(name = "candidate_statuses", indexes = {
        @Index(name = "idx_candidate_statuses_code", columnList = "code")
})
public class CandidateStatusMaster extends BaseMasterEntity {

    @Column(name = "terminal_status", nullable = false)
    private boolean terminalStatus;
}
