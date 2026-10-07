package com.arigs.rms.dto.request;

import com.arigs.rms.entity.OfferStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request to release or update an offer.
 */
public record OfferRequest(
        @NotNull @Positive BigDecimal offerAmount,
        @NotNull @FutureOrPresent LocalDate joiningDate,
        OfferStatus status
) {
}
