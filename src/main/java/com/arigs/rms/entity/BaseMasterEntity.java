package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Common structure for configurable master data.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseMasterEntity extends BaseAuditableEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 100;
}
