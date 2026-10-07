package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Single dynamic search filter.
 */
public record SearchFilter(
        @NotBlank String field,
        @NotNull SearchOperator operator,
        List<String> values
) {
}
