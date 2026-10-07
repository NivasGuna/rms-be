package com.arigs.rms.dto.response;

import java.util.List;

/**
 * Business rule evaluation result.
 */
public record BusinessRuleEvaluationResponse(
        String context,
        boolean passed,
        List<String> violations
) {
}
