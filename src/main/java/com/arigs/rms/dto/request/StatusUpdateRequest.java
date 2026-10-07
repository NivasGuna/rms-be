package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Generic status transition comments.
 */
public record StatusUpdateRequest(@NotBlank String comments) {
}
