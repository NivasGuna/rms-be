package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Fine-grained application permission.
 */
@Getter
@Setter
@Entity
@Table(name = "permissions", indexes = {
        @Index(name = "idx_permissions_code", columnList = "code"),
        @Index(name = "idx_permissions_resource_action", columnList = "resource,action")
})
public class Permission extends BaseMasterEntity {

    @Column(nullable = false, length = 80)
    private String resource;

    @Column(nullable = false, length = 60)
    private String action;
}
