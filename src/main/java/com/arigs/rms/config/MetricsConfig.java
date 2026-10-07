package com.arigs.rms.config;

import com.arigs.rms.repository.CandidateRepository;
import com.arigs.rms.repository.JobRequestRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers RMS business metrics for operational monitoring.
 */
@Configuration
public class MetricsConfig {

    @Bean
    public Object rmsBusinessMetrics(
            MeterRegistry meterRegistry,
            CandidateRepository candidateRepository,
            JobRequestRepository jobRequestRepository) {
        Gauge.builder("rms.candidates.active", candidateRepository, CandidateRepository::countByActiveTrueAndDeletedFalse)
                .description("Active non-deleted candidates")
                .register(meterRegistry);
        Gauge.builder("rms.positions.open", jobRequestRepository, JobRequestRepository::sumOpenPositions)
                .description("Open position count")
                .register(meterRegistry);
        return new Object();
    }
}
