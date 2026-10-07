package com.arigs.rms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Request for client contact maintenance.
 */
public record ClientContactRequest(
        @NotNull UUID clientId,
        @NotBlank @Size(max = 150) String contactName,
        @NotBlank @Email @Size(max = 150) String email,
        @Size(max = 30) String phone,
        @Size(max = 120) String designation,
        boolean primaryContact,
        Boolean active
) {
}
