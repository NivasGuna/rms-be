package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Persisted runtime configuration metadata and non-secret overrides.
 */
@Getter
@Setter
@Entity
@Table(name = "platform_runtime_config", indexes = {
        @Index(name = "idx_platform_runtime_config_active", columnList = "is_active, deleted")
})
public class PlatformRuntimeConfig extends BaseAuditableEntity {

    @Column(name = "config_key", nullable = false, unique = true, length = 160)
    private String configKey;

    @Column(name = "config_value", nullable = false, columnDefinition = "text")
    private String configValue;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private boolean secret;
}
