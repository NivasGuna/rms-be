package com.arigs.rms.dto.response;

import com.arigs.rms.entity.OfferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Offer response DTO.
 */
public record OfferResponse(
        UUID id,
        UUID candidateId,
        BigDecimal offerAmount,
        LocalDate joiningDate,
        OfferStatus status
) {
}
