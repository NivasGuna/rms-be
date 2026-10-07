package com.arigs.rms.dto.request;

import com.arigs.rms.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Request to update an RMS user.
 */
public record UserUpdateRequest(
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 150) String fullName,
        @NotEmpty Set<Role> roles,
        boolean active
) {
}
