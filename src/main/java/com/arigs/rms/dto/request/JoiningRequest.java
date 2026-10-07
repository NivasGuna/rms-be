package com.arigs.rms.dto.request;

import com.arigs.rms.entity.JoiningStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Request to confirm candidate joining.
 */
public record JoiningRequest(
        @NotNull LocalDate joiningDate,
        @NotNull JoiningStatus status,
        String remarks
) {
}
