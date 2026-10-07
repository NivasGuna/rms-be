package com.arigs.rms.dto.response;

import java.util.UUID;

/**
 * Result of a configured workflow transition.
 */
public record WorkflowTransitionResponse(
        UUID workflowActionId,
        String fromStepCode,
        String toStepCode,
        String toStepName,
        boolean approvalRecorded
) {
}
