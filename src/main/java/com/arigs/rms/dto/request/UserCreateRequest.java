package com.arigs.rms.dto.request;

import com.arigs.rms.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Request to create an RMS user.
 */
public record UserCreateRequest(
        @NotBlank @Size(max = 80) String username,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 40) String employeeCode,
        @NotBlank @Size(max = 150) String fullName,
        @NotBlank @Size(min = 12, max = 72) String password,
        @NotEmpty Set<Role> roles
) {
}
