package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Maps API route patterns to required permissions.
 */
@Getter
@Setter
@Entity
@Table(name = "api_permissions", indexes = {
        @Index(name = "idx_api_permissions_method_path", columnList = "http_method,path_pattern"),
        @Index(name = "idx_api_permissions_permission", columnList = "permission_id")
})
public class ApiPermission extends BaseAuditableEntity {

    @Column(name = "http_method", nullable = false, length = 12)
    private String httpMethod;

    @Column(name = "path_pattern", nullable = false, length = 250)
    private String pathPattern;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
