package com.arigs.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Starts the RMS backend application.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
@ConfigurationPropertiesScan
public class RmsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmsBackendApplication.class, args);
    }
}
