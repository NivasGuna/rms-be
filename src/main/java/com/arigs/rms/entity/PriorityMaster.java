package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Priority master.
 */
@Getter
@Setter
@Entity
@Table(name = "priorities", indexes = {
        @Index(name = "idx_priorities_code", columnList = "code")
})
public class PriorityMaster extends BaseMasterEntity {

    @Column(name = "sla_hours")
    private Integer slaHours;
}
