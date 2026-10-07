package com.arigs.rms.dto.response;

import java.util.UUID;

/**
 * Client contact response DTO.
 */
public record ClientContactResponse(
        UUID id,
        UUID clientId,
        String clientName,
        String contactName,
        String email,
        String phone,
        String designation,
        boolean primaryContact,
        boolean active
) {
}
