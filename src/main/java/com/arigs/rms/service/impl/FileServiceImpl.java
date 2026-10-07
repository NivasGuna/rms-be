package com.arigs.rms.service.impl;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.dto.response.FileMetadataResponse;
import com.arigs.rms.entity.FileMetadata;
import com.arigs.rms.entity.FileStorageStrategy;
import com.arigs.rms.entity.VirusScanStatus;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.mapper.FileMetadataMapper;
import com.arigs.rms.repository.FileMetadataRepository;
import com.arigs.rms.security.UserPrincipal;
import com.arigs.rms.service.FileService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Stores uploaded files on disk and metadata in PostgreSQL.
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final FileMetadataRepository fileMetadataRepository;
    private final FileMetadataMapper mapper;
    private final RmsProperties properties;

    @Override
    @Transactional
    public FileMetadataResponse upload(String entityType, UUID entityId, MultipartFile file) {
        validateFile(file);
        UUID uploadedBy = currentUserId();
        try {
            String storageRoot = properties.getFiles().getStorageRoot();
            Files.createDirectories(Path.of(storageRoot, entityType, entityId.toString()));
            String extension = extension(file.getOriginalFilename());
            String storedName = UUID.randomUUID() + "." + extension;
            Path target = Path.of(storageRoot, entityType, entityId.toString(), storedName);
            String checksum = copyWithChecksum(file, target);

            FileMetadata metadata = new FileMetadata();
            metadata.setEntityType(entityType);
            metadata.setEntityId(entityId);
            metadata.setOriginalFileName(file.getOriginalFilename());
            metadata.setStoredFileName(storedName);
            metadata.setContentType(file.getContentType());
            metadata.setFileSizeBytes(file.getSize());
            metadata.setStoragePath(target.toAbsolutePath().normalize().toString());
            metadata.setUploadedByUserId(uploadedBy);
            metadata.setSha256Checksum(checksum);
            metadata.setFileVersion((int) fileMetadataRepository.countByEntityTypeAndEntityIdAndActiveTrueAndDeletedFalse(entityType, entityId) + 1);
            metadata.setDuplicateOfFile(fileMetadataRepository
                    .findFirstBySha256ChecksumAndActiveTrueAndDeletedFalseOrderByCreatedDateAsc(checksum)
                    .orElse(null));
            metadata.setStorageStrategy(FileStorageStrategy.LOCAL_DISK);
            metadata.setVirusScanStatus(VirusScanStatus.PENDING);
            return mapper.toResponse(fileMetadataRepository.save(metadata));
        } catch (IOException ex) {
            throw new BusinessException("File upload failed");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetadataResponse> list(String entityType, UUID entityId) {
        return fileMetadataRepository.findByEntityTypeAndEntityIdAndActiveTrueOrderByCreatedDateDesc(entityType, entityId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty");
        }
        if (!ALLOWED_EXTENSIONS.contains(extension(file.getOriginalFilename()))) {
            throw new BusinessException("Only PDF, DOC and DOCX files are allowed");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BusinessException("Unsupported file content type");
        }
    }

    private String extension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BusinessException("File extension is required");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private String copyWithChecksum(MultipartFile file, Path target) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream inputStream = file.getInputStream();
                 DigestInputStream digestStream = new DigestInputStream(inputStream, digest)) {
                Files.copy(digestStream, target);
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", ex);
        }
    }

    private UUID currentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }
        throw new ResourceNotFoundException("Authenticated user not found");
    }
}
