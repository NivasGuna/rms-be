package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Business unit master.
 */
@Getter
@Setter
@Entity
@Table(name = "business_units", indexes = {
        @Index(name = "idx_business_units_code", columnList = "code"),
        @Index(name = "idx_business_units_active", columnList = "is_active")
})
public class BusinessUnit extends BaseMasterEntity {
}
