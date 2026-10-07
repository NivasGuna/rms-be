package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

/**
 * Request to evaluate and record a workflow transition.
 */
public record WorkflowTransitionRequest(
        @NotBlank String entityType,
        @NotNull UUID entityId,
        @NotBlank String currentStepCode,
        @NotBlank String actionCode,
        String comments,
        Map<String, String> facts
) {
}
