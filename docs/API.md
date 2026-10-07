# RMS Backend API

This backend exposes a versioned REST API under `/api/v1`, OpenAPI metadata at `/api-docs`, Swagger UI at `/swagger-ui.html`, and production health checks through Spring Boot actuator.

## Authentication

All `/api/v1/**` endpoints require a JWT bearer token except `/api/v1/auth/**`. Public operational endpoints are limited to `GET /actuator/health/**`, `GET /actuator/info`, `/api-docs/**`, and Swagger UI.

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/api/v1/auth/login` | Authenticate and issue access and refresh tokens. |
| POST | `/api/v1/auth/refresh` | Exchange a refresh token for a new token pair. |
| POST | `/api/v1/auth/logout` | Revoke a refresh token. |

## Recruitment APIs

| Area | Endpoints |
| --- | --- |
| Job requests | `POST /api/v1/job-requests`, `GET /api/v1/job-requests`, `GET /api/v1/job-requests/{id}`, `POST /api/v1/job-requests/{id}/approve`, `POST /api/v1/job-requests/{id}/reject`, `POST /api/v1/job-requests/{id}/tag-manager`, `POST /api/v1/job-requests/{id}/tag-associate`, `POST /api/v1/job-requests/{id}/start-sourcing`, `POST /api/v1/job-requests/{id}/close`, `GET /api/v1/job-requests/{id}/history` |
| Candidates | `POST /api/v1/candidates`, `GET /api/v1/candidates`, `GET /api/v1/candidates/{id}`, `POST /api/v1/candidates/{id}/status`, `GET /api/v1/candidates/{id}/history`, `POST /api/v1/candidates/{id}/interviews`, `GET /api/v1/candidates/{id}/interviews`, `POST /api/v1/candidates/{id}/offer`, `POST /api/v1/candidates/{id}/joining` |
| Users | `POST /api/v1/users`, `PUT /api/v1/users/{id}`, `GET /api/v1/users`, `GET /api/v1/users/{id}` |
| Dashboard | `GET /api/v1/dashboard/kpis`, `GET /api/v1/dashboard/candidate-pipeline`, `GET /api/v1/dashboard/interview-pipeline`, `GET /api/v1/dashboard/offer-pipeline`, `GET /api/v1/dashboard/joining-statistics`, `GET /api/v1/dashboard/monthly-hiring`, `GET /api/v1/dashboard/recruiter-performance` |
| Notifications | `POST /api/v1/notifications/email`, `POST /api/v1/notifications/retry`, `POST /api/v1/notification-center`, `GET /api/v1/notification-center/users/{userId}`, `GET /api/v1/notification-center/users/{userId}/unread-count`, `POST /api/v1/notification-center/{notificationId}/read`, `POST /api/v1/notification-center/preferences` |
| Files | `POST /api/v1/files/{entityType}/{entityId}`, `GET /api/v1/files/{entityType}/{entityId}` |
| Workflow and audit | `POST /api/v1/workflow-engine/transition`, `POST /api/v1/workflow-engine/rules/evaluate`, `GET /api/v1/timelines/jobs/{jobRequestId}`, `GET /api/v1/timelines/candidates/{candidateId}`, `POST /api/v1/audit-trails/search` |
| Reporting and jobs | `POST /api/v1/reports/export`, `POST /api/v1/background-jobs/{jobCode}/run`, `GET /api/v1/background-jobs/executions` |

## Platform APIs

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/v1/platform/feature-flags` | Returns normalized configured feature flags. |
| GET | `/api/v1/platform/config` | Returns sanitized runtime configuration, including secret redaction and persisted config metadata. |

## Operations

| Endpoint | Purpose |
| --- | --- |
| `GET /actuator/health/liveness` | Kubernetes liveness probe. |
| `GET /actuator/health/readiness` | Readiness probe including database, disk, outbox, storage, and integration checks. |
| `GET /actuator/prometheus` | Prometheus metrics scrape endpoint. |
| `GET /api-docs` | Complete OpenAPI document. |
| `GET /api-docs/rms-business` | Business API OpenAPI group. |
| `GET /api-docs/rms-platform` | Platform and operations API OpenAPI group. |

## Platform Hardening Notes

Domain events are persisted to `outbox_events` and dispatched by `OutboxDispatcher`. The default integration publisher acknowledges events without external side effects, which keeps local and production deployments safe until a broker or webhook adapter is added behind `IntegrationEventPublisher`.

Feature flags are configured under `rms.platform.feature-flags`. Current platform flags are `outbox-dispatch` and `external-integrations`.

OpenTelemetry is enabled through Micrometer tracing. Configure `RMS_OTEL_EXPORTER_OTLP_ENDPOINT` and `RMS_TRACING_SAMPLING_PROBABILITY` for production collectors.
