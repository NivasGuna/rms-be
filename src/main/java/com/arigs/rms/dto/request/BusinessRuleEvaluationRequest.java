package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * Request to evaluate business rules for a context.
 */
public record BusinessRuleEvaluationRequest(
        @NotBlank String context,
        @NotEmpty Map<String, String> facts
) {
}
