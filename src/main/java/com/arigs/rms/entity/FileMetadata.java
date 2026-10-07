package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Metadata for uploaded business files.
 */
@Getter
@Setter
@Entity
@Table(name = "file_metadata", indexes = {
        @Index(name = "idx_file_metadata_owner", columnList = "entity_type,entity_id"),
        @Index(name = "idx_file_metadata_uploaded_by", columnList = "uploaded_by_user_id")
})
public class FileMetadata extends BaseAuditableEntity {

    @Column(name = "entity_type", nullable = false, length = 60)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "stored_file_name", nullable = false, unique = true, length = 255)
    private String storedFileName;

    @Column(name = "content_type", nullable = false, length = 120)
    private String contentType;

    @Column(name = "file_size_bytes", nullable = false)
    private long fileSizeBytes;

    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "uploaded_by_user_id", nullable = false)
    private UUID uploadedByUserId;

    @Column(name = "sha256_checksum", nullable = false, length = 64)
    private String sha256Checksum;

    @Column(name = "file_version", nullable = false)
    private int fileVersion = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duplicate_of_file_id")
    private FileMetadata duplicateOfFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_strategy", nullable = false, length = 30)
    private FileStorageStrategy storageStrategy = FileStorageStrategy.LOCAL_DISK;

    @Enumerated(EnumType.STRING)
    @Column(name = "virus_scan_status", nullable = false, length = 30)
    private VirusScanStatus virusScanStatus = VirusScanStatus.PENDING;
}
