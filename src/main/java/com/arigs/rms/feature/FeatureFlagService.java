package com.arigs.rms.feature;

import java.util.Map;

/**
 * Reads runtime feature flags without coupling business code to property sources.
 */
public interface FeatureFlagService {

    boolean isEnabled(String flagName);

    Map<String, Boolean> all();
}
