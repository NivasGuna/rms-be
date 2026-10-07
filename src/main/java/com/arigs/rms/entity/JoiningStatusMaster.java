package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Joining status master.
 */
@Getter
@Setter
@Entity
@Table(name = "joining_statuses", indexes = {
        @Index(name = "idx_joining_statuses_code", columnList = "code")
})
public class JoiningStatusMaster extends BaseMasterEntity {

    @Column(name = "terminal_status", nullable = false)
    private boolean terminalStatus;
}
