package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Configurable business rule expressed as a simple field/operator/value condition.
 */
@Getter
@Setter
@Entity
@Table(name = "business_rules", indexes = {
        @Index(name = "idx_business_rules_code", columnList = "code"),
        @Index(name = "idx_business_rules_context", columnList = "context")
})
public class BusinessRule extends BaseMasterEntity {

    @Column(nullable = false, length = 80)
    private String context;

    @Column(name = "field_name", nullable = false, length = 80)
    private String fieldName;

    @Column(nullable = false, length = 30)
    private String operator;

    @Column(name = "expected_value", nullable = false, length = 200)
    private String expectedValue;

    @Column(name = "failure_message", nullable = false, length = 250)
    private String failureMessage;
}
