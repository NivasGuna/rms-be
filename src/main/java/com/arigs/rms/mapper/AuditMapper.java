package com.arigs.rms.mapper;

import com.arigs.rms.dto.response.AuditTrailResponse;
import com.arigs.rms.entity.AuditLog;
import org.mapstruct.Mapper;

/**
 * Maps audit logs to response DTOs.
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    default AuditTrailResponse toResponse(AuditLog auditLog) {
        return new AuditTrailResponse(
                auditLog.getId(),
                auditLog.getEventType(),
                auditLog.getActor(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getEntityId(),
                auditLog.getDetails(),
                auditLog.getCreatedDate());
    }
}
