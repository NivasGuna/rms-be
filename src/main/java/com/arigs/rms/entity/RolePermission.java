package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Maps application roles to permissions.
 */
@Getter
@Setter
@Entity
@Table(name = "role_permissions", indexes = {
        @Index(name = "idx_role_permissions_role", columnList = "role"),
        @Index(name = "idx_role_permissions_permission", columnList = "permission_id")
})
public class RolePermission extends BaseAuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
