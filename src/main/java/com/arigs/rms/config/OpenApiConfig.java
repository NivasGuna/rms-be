package com.arigs.rms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

/**
 * Configures OpenAPI metadata and JWT bearer authentication.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rmsOpenApi() {
        String schemeName = "bearerAuth";
        Components components = new Components()
                .addSecuritySchemes(schemeName, new SecurityScheme()
                        .name(schemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSchemas("outbox", new Schema<>().type("object")
                        .description("Durable outbox integration configuration"))
                .addSchemas("storage", new Schema<>().type("object")
                        .description("File storage configuration"))
                .addSchemas("integrations", new Schema<>().type("object")
                        .description("Outbound integration configuration"));

        return new OpenAPI()
                .info(new Info()
                        .title("RMS Backend API")
                        .version("v1")
                        .description("Resource Management System APIs with authentication, recruitment workflows, platform operations, and actuator-backed production readiness endpoints.")
                        .contact(new Contact().name("Automotive Robotics India Pvt Ltd.")))
                .addServersItem(new Server().url("/").description("Current environment"))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(components);
    }

    @Bean
    public GroupedOpenApi rmsBusinessApi() {
        return GroupedOpenApi.builder()
                .group("rms-business")
                .pathsToMatch("/api/v1/**")
                .pathsToExclude("/api/v1/platform/**")
                .build();
    }

    @Bean
    public GroupedOpenApi rmsPlatformApi() {
        return GroupedOpenApi.builder()
                .group("rms-platform")
                .pathsToMatch("/api/v1/platform/**", "/actuator/health/**", "/actuator/info")
                .build();
    }
}
