package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Approval or rejection comments.
 */
public record DecisionRequest(@NotBlank String comments) {
}
