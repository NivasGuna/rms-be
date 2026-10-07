package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.UserCreateRequest;
import com.arigs.rms.dto.request.UserUpdateRequest;
import com.arigs.rms.dto.response.UserResponse;
import com.arigs.rms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User administration API.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create RMS user")
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "User created", userService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update RMS user")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User updated", userService.update(id, request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Get RMS user")
    public ResponseEntity<ApiResponse<UserResponse>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User retrieved", userService.get(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER')")
    @Operation(summary = "Search RMS users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> search(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Users retrieved", userService.search(keyword, pageable)));
    }
}
