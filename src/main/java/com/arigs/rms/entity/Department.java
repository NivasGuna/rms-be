package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Department master scoped to a business unit.
 */
@Getter
@Setter
@Entity
@Table(name = "departments", indexes = {
        @Index(name = "idx_departments_code", columnList = "code"),
        @Index(name = "idx_departments_business_unit", columnList = "business_unit_id")
})
public class Department extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_unit_id")
    private BusinessUnit businessUnit;
}
