package com.arigs.rms.feature;

import com.arigs.rms.config.RmsProperties;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Feature flag implementation backed by centralized RMS configuration.
 */
@Service
@RequiredArgsConstructor
public class ConfigFeatureFlagService implements FeatureFlagService {

    private final RmsProperties properties;

    @Override
    public boolean isEnabled(String flagName) {
        if (flagName == null || flagName.isBlank()) {
            return false;
        }
        return all().getOrDefault(normalize(flagName), false);
    }

    @Override
    public Map<String, Boolean> all() {
        Map<String, Boolean> flags = new TreeMap<>();
        properties.getPlatform().getFeatureFlags()
                .forEach((key, value) -> flags.put(normalize(key), Boolean.TRUE.equals(value)));
        return Map.copyOf(flags);
    }

    private String normalize(String flagName) {
        return flagName.trim().toLowerCase(Locale.ROOT).replace('_', '-');
    }
}
