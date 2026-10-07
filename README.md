# RMS Backend

Spring Boot 3 / Java 21 backend for the RMS Resource Management System.

## Local Run

1. Create a PostgreSQL 17 database named `rms`.
2. Set environment variables for `RMS_DB_USERNAME`, `RMS_DB_PASSWORD`, `RMS_JWT_SECRET`, and SMTP values as needed.
3. Run:

```powershell
mvn spring-boot:run
```

Swagger UI is available at `/swagger-ui.html` after startup.

## Platform Operations

- OpenAPI JSON: `/api-docs`
- Swagger UI: `/swagger-ui.html`
- Readiness: `/actuator/health/readiness`
- Liveness: `/actuator/health/liveness`
- Metrics: `/actuator/prometheus`
- Postman collection: `docs/postman/rms-backend.postman_collection.json`
- API guide: `docs/API.md`

The backend includes a durable domain-event outbox, feature flags under `rms.platform.feature-flags`, sanitized runtime configuration at `/api/v1/platform/config`, and OpenTelemetry tracing through Micrometer.
