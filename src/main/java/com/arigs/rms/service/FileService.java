package com.arigs.rms.service;

import com.arigs.rms.dto.response.FileMetadataResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * File upload use cases.
 */
public interface FileService {

    FileMetadataResponse upload(String entityType, UUID entityId, MultipartFile file);

    List<FileMetadataResponse> list(String entityType, UUID entityId);
}
