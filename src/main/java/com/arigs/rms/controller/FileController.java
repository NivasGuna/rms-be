package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.dto.response.FileMetadataResponse;
import com.arigs.rms.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * File upload API.
 */
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "Files")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/{entityType}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','TAG_MANAGER','TAG_ASSOCIATE')")
    @Operation(summary = "Upload PDF, DOC or DOCX file")
    public ResponseEntity<ApiResponse<FileMetadataResponse>> upload(
            @PathVariable String entityType,
            @PathVariable UUID entityId,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "File uploaded", fileService.upload(entityType, entityId, file)));
    }

    @GetMapping("/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "List uploaded file metadata")
    public ResponseEntity<ApiResponse<List<FileMetadataResponse>>> list(
            @PathVariable String entityType,
            @PathVariable UUID entityId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Files retrieved", fileService.list(entityType, entityId)));
    }
}
