package com.arigs.rms.health;

import com.arigs.rms.config.RmsProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Verifies that the configured file storage root is writable.
 */
@Component("storage")
@RequiredArgsConstructor
public class StorageHealthIndicator implements HealthIndicator {

    private final RmsProperties properties;

    @Override
    public Health health() {
        try {
            Path storageRoot = Path.of(properties.getFiles().getStorageRoot()).toAbsolutePath().normalize();
            Files.createDirectories(storageRoot);
            boolean writable = Files.isWritable(storageRoot);
            Health.Builder builder = writable ? Health.up() : Health.down();
            return builder.withDetail("path", storageRoot.toString()).withDetail("writable", writable).build();
        } catch (RuntimeException | java.io.IOException ex) {
            return Health.down(ex).withDetail("path", properties.getFiles().getStorageRoot()).build();
        }
    }
}
