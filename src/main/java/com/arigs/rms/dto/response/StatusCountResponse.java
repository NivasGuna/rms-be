package com.arigs.rms.dto.response;

/**
 * Count grouped by a workflow status.
 */
public record StatusCountResponse(String status, long total) {
}
