package com.arigs.rms.repository;

import com.arigs.rms.entity.FileMetadata;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for uploaded file metadata.
 */
public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {

    List<FileMetadata> findByEntityTypeAndEntityIdAndActiveTrueOrderByCreatedDateDesc(String entityType, UUID entityId);

    Optional<FileMetadata> findFirstBySha256ChecksumAndActiveTrueAndDeletedFalseOrderByCreatedDateAsc(String sha256Checksum);

    long countByEntityTypeAndEntityIdAndActiveTrueAndDeletedFalse(String entityType, UUID entityId);
}
