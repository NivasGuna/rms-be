package com.arigs.rms.mapper;

import com.arigs.rms.dto.response.FileMetadataResponse;
import com.arigs.rms.entity.FileMetadata;
import org.mapstruct.Mapper;

/**
 * Maps uploaded file metadata to DTOs.
 */
@Mapper(componentModel = "spring")
public interface FileMetadataMapper {

    default FileMetadataResponse toResponse(FileMetadata metadata) {
        return new FileMetadataResponse(
                metadata.getId(),
                metadata.getEntityType(),
                metadata.getEntityId(),
                metadata.getOriginalFileName(),
                metadata.getContentType(),
                metadata.getFileSizeBytes(),
                metadata.getSha256Checksum(),
                metadata.getFileVersion(),
                metadata.getDuplicateOfFile() != null,
                metadata.getStorageStrategy().name(),
                metadata.getVirusScanStatus().name());
    }
}
