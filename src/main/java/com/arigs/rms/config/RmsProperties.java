package com.arigs.rms.config;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Centralized RMS runtime configuration bound from application properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rms")
public class RmsProperties {

    private Security security = new Security();
    private Files files = new Files();
    private Notification notification = new Notification();
    private Outbox outbox = new Outbox();
    private Platform platform = new Platform();
    private Integrations integrations = new Integrations();

    @Getter
    @Setter
    public static class Security {
        private String jwtSecret;
        private long accessTokenMinutes = 30;
        private long refreshTokenDays = 7;
    }

    @Getter
    @Setter
    public static class Files {
        private String storageRoot = "./storage";
    }

    @Getter
    @Setter
    public static class Notification {
        private int retryLimit = 3;
        private long retryDelayMs = 300000;
    }

    @Getter
    @Setter
    public static class Outbox {
        private boolean enabled = true;
        private int batchSize = 50;
        private int retryLimit = 10;
        private long dispatchDelayMs = 30000;
        private Duration staleAfter = Duration.ofMinutes(15);
    }

    @Getter
    @Setter
    public static class Platform {
        private Map<String, Boolean> featureFlags = new LinkedHashMap<>();
    }

    @Getter
    @Setter
    public static class Integrations {
        private boolean outboundEnabled = true;
        private Duration timeout = Duration.ofSeconds(5);
        private Map<String, String> endpoints = new LinkedHashMap<>();
    }
}
