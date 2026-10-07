package com.arigs.rms.dto.response;

import com.arigs.rms.entity.Role;
import java.util.Set;
import java.util.UUID;

/**
 * User response DTO.
 */
public record UserResponse(
        UUID id,
        String username,
        String email,
        String employeeCode,
        String fullName,
        Set<Role> roles,
        boolean active
) {
}
