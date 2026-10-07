package com.arigs.rms.integration;

import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.arigs.rms.feature.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:rms_platform;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false",
        "spring.quartz.job-store-type=memory",
        "spring.task.scheduling.enabled=false",
        "management.tracing.enabled=false",
        "rms.security.jwt-secret=0123456789012345678901234567890101234567890123456789012345678901",
        "rms.files.storage-root=target/test-storage/rms-platform-test",
        "rms.platform.feature-flags.outbox-dispatch=true",
        "rms.platform.feature-flags.external-integrations=false"
})
class PlatformOperationalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeatureFlagService featureFlagService;

    @Test
    void exposesProductionHealthAndOpenApiWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.schemas").exists())
                .andExpect(jsonPath("$.info.title").value("RMS Backend API"));
    }

    @Test
    void bindsFeatureFlagsFromCentralConfiguration() {
        org.assertj.core.api.Assertions.assertThat(featureFlagService.isEnabled("outbox_dispatch")).isTrue();
        org.assertj.core.api.Assertions.assertThat(featureFlagService.isEnabled("external-integrations")).isFalse();
    }
}
