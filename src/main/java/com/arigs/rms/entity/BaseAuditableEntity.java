package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;

/**
 * Common columns required by all persisted RMS tables.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy = "system";

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false, length = 100)
    private String updatedBy = "system";

    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = Instant.now();

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @Version
    @Column(nullable = false)
    private long version;

    /**
     * Marks the entity as deleted while preserving its database row for audit and history.
     */
    public void markDeleted(String actor, Instant deletedAt) {
        this.active = false;
        this.deleted = true;
        this.deletedBy = actor;
        this.deletedDate = deletedAt;
    }
}
