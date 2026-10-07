package com.arigs.rms.config;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.ObservationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Adds RMS service metadata to Micrometer observations exported through OpenTelemetry.
 */
@Configuration
public class TelemetryConfig {

    @Bean
    public ObservationFilter rmsObservationFilter() {
        return context -> {
            context.addLowCardinalityKeyValue(KeyValue.of("service.namespace", "rms"));
            context.addLowCardinalityKeyValue(KeyValue.of("service.name", "rms-backend"));
            return context;
        };
    }
}
