package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.feature.FeatureFlagService;
import com.arigs.rms.platform.RuntimeConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Platform operations API for runtime configuration and feature flag visibility.
 */
@RestController
@RequestMapping("/api/v1/platform")
@RequiredArgsConstructor
@Tag(name = "Platform")
public class PlatformController {

    private final FeatureFlagService featureFlagService;
    private final RuntimeConfigurationService runtimeConfigurationService;

    @GetMapping("/feature-flags")
    @Operation(summary = "List configured feature flags")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> featureFlags() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Feature flags loaded", featureFlagService.all()));
    }

    @GetMapping("/config")
    @Operation(summary = "Read sanitized runtime configuration")
    public ResponseEntity<ApiResponse<Map<String, Object>>> configuration() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Runtime configuration loaded", runtimeConfigurationService.snapshot()));
    }
}
