package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * TAG assignment request.
 */
public record AssignmentRequest(@NotNull UUID userId, String comments) {
}
