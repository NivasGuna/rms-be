package com.arigs.rms.platform;

import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.entity.PlatformRuntimeConfig;
import com.arigs.rms.repository.PlatformRuntimeConfigRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Provides a sanitized operational view of runtime configuration.
 */
@Service
@RequiredArgsConstructor
public class RuntimeConfigurationService {

    private final RmsProperties properties;
    private final Environment environment;
    private final PlatformRuntimeConfigRepository runtimeConfigRepository;

    public Map<String, Object> snapshot() {
        return Map.of(
                "application", environment.getProperty("spring.application.name", "rms-backend"),
                "profiles", environment.getActiveProfiles(),
                "security", Map.of(
                        "accessTokenMinutes", properties.getSecurity().getAccessTokenMinutes(),
                        "refreshTokenDays", properties.getSecurity().getRefreshTokenDays(),
                        "jwtSecretConfigured", properties.getSecurity().getJwtSecret() != null
                                && !properties.getSecurity().getJwtSecret().isBlank()),
                "files", Map.of("storageRoot", properties.getFiles().getStorageRoot()),
                "notification", Map.of(
                        "retryLimit", properties.getNotification().getRetryLimit(),
                        "retryDelayMs", properties.getNotification().getRetryDelayMs()),
                "outbox", Map.of(
                        "enabled", properties.getOutbox().isEnabled(),
                        "batchSize", properties.getOutbox().getBatchSize(),
                        "retryLimit", properties.getOutbox().getRetryLimit(),
                        "dispatchDelayMs", properties.getOutbox().getDispatchDelayMs()),
                "integrations", Map.of(
                        "outboundEnabled", properties.getIntegrations().isOutboundEnabled(),
                        "timeout", properties.getIntegrations().getTimeout().toString(),
                        "configuredEndpoints", properties.getIntegrations().getEndpoints().keySet()),
                "persistedConfig", runtimeConfigRepository.findByActiveTrueAndDeletedFalseOrderByConfigKeyAsc().stream()
                        .collect(Collectors.toMap(
                                PlatformRuntimeConfig::getConfigKey,
                                config -> config.isSecret() ? "[redacted]" : config.getConfigValue(),
                                (left, right) -> right)));
    }
}
