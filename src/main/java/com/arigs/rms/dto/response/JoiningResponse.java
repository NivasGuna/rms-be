package com.arigs.rms.dto.response;

import com.arigs.rms.entity.JoiningStatus;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Joining response DTO.
 */
public record JoiningResponse(
        UUID id,
        UUID candidateId,
        LocalDate joiningDate,
        JoiningStatus status,
        String remarks
) {
}
