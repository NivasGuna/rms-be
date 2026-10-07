package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Employment type master for database-driven administration.
 */
@Getter
@Setter
@Entity
@Table(name = "employment_types", indexes = {
        @Index(name = "idx_employment_types_code", columnList = "code")
})
public class EmploymentTypeMaster extends BaseMasterEntity {
}
