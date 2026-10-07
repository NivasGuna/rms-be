package com.arigs.rms.dto.response;

import java.util.UUID;

/**
 * Uploaded file metadata response.
 */
public record FileMetadataResponse(
        UUID id,
        String entityType,
        UUID entityId,
        String originalFileName,
        String contentType,
        long fileSizeBytes,
        String sha256Checksum,
        int fileVersion,
        boolean duplicate,
        String storageStrategy,
        String virusScanStatus
) {
}
