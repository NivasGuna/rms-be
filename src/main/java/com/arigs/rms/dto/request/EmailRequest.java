package com.arigs.rms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * Request to send a templated email.
 */
public record EmailRequest(
        @NotBlank @Email String recipientEmail,
        @NotBlank String subject,
        @NotBlank String htmlTemplate,
        @NotEmpty Map<String, String> variables
) {
}
