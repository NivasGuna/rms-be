package com.arigs.rms.dto.response;

import com.arigs.rms.common.MasterDataType;
import java.util.UUID;

/**
 * Master-data response DTO.
 */
public record MasterDataResponse(
        UUID id,
        MasterDataType type,
        String code,
        String name,
        String description,
        int sortOrder,
        boolean active,
        UUID parentId,
        String parentName,
        String category,
        String city,
        String state,
        String country,
        Integer slaHours,
        Boolean terminalStatus,
        String subject,
        String route,
        String resource,
        String action
) {
}
