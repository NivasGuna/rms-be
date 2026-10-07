package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Configurable job status master.
 */
@Getter
@Setter
@Entity
@Table(name = "job_statuses", indexes = {
        @Index(name = "idx_job_statuses_code", columnList = "code")
})
public class JobStatusMaster extends BaseMasterEntity {

    @Column(name = "terminal_status", nullable = false)
    private boolean terminalStatus;
}
