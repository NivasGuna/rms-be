package com.arigs.rms.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Unified workflow timeline event.
 */
public record WorkflowTimelineResponse(
        Instant eventDate,
        String eventType,
        String entityType,
        UUID entityId,
        String oldStatus,
        String newStatus,
        String actor,
        String details
) implements Comparable<WorkflowTimelineResponse> {

    @Override
    public int compareTo(WorkflowTimelineResponse other) {
        return this.eventDate.compareTo(other.eventDate);
    }
}
