package com.arigs.rms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * Request for creating or updating master data.
 */
public record MasterDataRequest(
        @NotBlank @Size(max = 60) String code,
        @NotBlank @Size(max = 150) String name,
        String description,
        Integer sortOrder,
        Boolean active,
        UUID businessUnitId,
        UUID departmentId,
        UUID parentMenuId,
        UUID notificationTypeId,
        @Size(max = 30) String gstNumber,
        @Size(max = 200) String website,
        @Size(max = 80) String category,
        @Size(max = 100) String city,
        @Size(max = 100) String state,
        @Size(max = 100) String country,
        Integer slaHours,
        Boolean terminalStatus,
        @Size(max = 180) String subject,
        String htmlBody,
        @Size(max = 200) String route,
        @Size(max = 80) String icon,
        @Size(max = 80) String resource,
        @Size(max = 60) String action
) {
}
