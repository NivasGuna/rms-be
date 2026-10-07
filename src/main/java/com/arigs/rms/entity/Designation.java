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
 * Designation master scoped to a department.
 */
@Getter
@Setter
@Entity
@Table(name = "designations", indexes = {
        @Index(name = "idx_designations_code", columnList = "code"),
        @Index(name = "idx_designations_department", columnList = "department_id")
})
public class Designation extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
