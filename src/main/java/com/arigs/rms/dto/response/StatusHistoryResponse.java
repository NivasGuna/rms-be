package com.arigs.rms.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Status history response.
 */
public record StatusHistoryResponse(
        UUID id,
        String oldStatus,
        String newStatus,
        String comments,
        String changedBy,
        Instant changedDate
) {
}
