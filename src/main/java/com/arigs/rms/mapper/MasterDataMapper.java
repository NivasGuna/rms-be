package com.arigs.rms.mapper;

import com.arigs.rms.common.MasterDataType;
import com.arigs.rms.dto.response.ClientContactResponse;
import com.arigs.rms.dto.response.MasterDataResponse;
import com.arigs.rms.entity.BaseMasterEntity;
import com.arigs.rms.entity.CandidateStatusMaster;
import com.arigs.rms.entity.Client;
import com.arigs.rms.entity.ClientContact;
import com.arigs.rms.entity.Department;
import com.arigs.rms.entity.Designation;
import com.arigs.rms.entity.EmailTemplate;
import com.arigs.rms.entity.JobStatusMaster;
import com.arigs.rms.entity.JoiningStatusMaster;
import com.arigs.rms.entity.Menu;
import com.arigs.rms.entity.OfferStatusMaster;
import com.arigs.rms.entity.Permission;
import com.arigs.rms.entity.PriorityMaster;
import com.arigs.rms.entity.RmsLocation;
import com.arigs.rms.entity.Skill;
import java.util.UUID;
import org.mapstruct.Mapper;

/**
 * Maps master-data entities into API DTOs.
 */
@Mapper(componentModel = "spring")
public interface MasterDataMapper {

    default MasterDataResponse toResponse(MasterDataType type, BaseMasterEntity entity) {
        return new MasterDataResponse(
                entity.getId(),
                type,
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getSortOrder(),
                entity.isActive(),
                parentId(entity),
                parentName(entity),
                category(entity),
                city(entity),
                state(entity),
                country(entity),
                slaHours(entity),
                terminalStatus(entity),
                subject(entity),
                route(entity),
                resource(entity),
                action(entity));
    }

    default ClientContactResponse toClientContactResponse(ClientContact contact) {
        return new ClientContactResponse(
                contact.getId(),
                contact.getClient().getId(),
                contact.getClient().getName(),
                contact.getContactName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getDesignation(),
                contact.isPrimaryContact(),
                contact.isActive());
    }

    private UUID parentId(BaseMasterEntity entity) {
        if (entity instanceof Department department && department.getBusinessUnit() != null) {
            return department.getBusinessUnit().getId();
        }
        if (entity instanceof Designation designation && designation.getDepartment() != null) {
            return designation.getDepartment().getId();
        }
        if (entity instanceof Client client && client.getBusinessUnit() != null) {
            return client.getBusinessUnit().getId();
        }
        if (entity instanceof EmailTemplate template && template.getNotificationType() != null) {
            return template.getNotificationType().getId();
        }
        if (entity instanceof Menu menu && menu.getParentMenu() != null) {
            return menu.getParentMenu().getId();
        }
        return null;
    }

    private String parentName(BaseMasterEntity entity) {
        if (entity instanceof Department department && department.getBusinessUnit() != null) {
            return department.getBusinessUnit().getName();
        }
        if (entity instanceof Designation designation && designation.getDepartment() != null) {
            return designation.getDepartment().getName();
        }
        if (entity instanceof Client client && client.getBusinessUnit() != null) {
            return client.getBusinessUnit().getName();
        }
        if (entity instanceof EmailTemplate template && template.getNotificationType() != null) {
            return template.getNotificationType().getName();
        }
        if (entity instanceof Menu menu && menu.getParentMenu() != null) {
            return menu.getParentMenu().getName();
        }
        return null;
    }

    private String category(BaseMasterEntity entity) {
        return entity instanceof Skill skill ? skill.getCategory() : null;
    }

    private String city(BaseMasterEntity entity) {
        return entity instanceof RmsLocation location ? location.getCity() : null;
    }

    private String state(BaseMasterEntity entity) {
        return entity instanceof RmsLocation location ? location.getState() : null;
    }

    private String country(BaseMasterEntity entity) {
        return entity instanceof RmsLocation location ? location.getCountry() : null;
    }

    private Integer slaHours(BaseMasterEntity entity) {
        return entity instanceof PriorityMaster priority ? priority.getSlaHours() : null;
    }

    private Boolean terminalStatus(BaseMasterEntity entity) {
        if (entity instanceof JobStatusMaster status) {
            return status.isTerminalStatus();
        }
        if (entity instanceof CandidateStatusMaster status) {
            return status.isTerminalStatus();
        }
        if (entity instanceof OfferStatusMaster status) {
            return status.isTerminalStatus();
        }
        if (entity instanceof JoiningStatusMaster status) {
            return status.isTerminalStatus();
        }
        return null;
    }

    private String subject(BaseMasterEntity entity) {
        return entity instanceof EmailTemplate template ? template.getSubject() : null;
    }

    private String route(BaseMasterEntity entity) {
        return entity instanceof Menu menu ? menu.getRoute() : null;
    }

    private String resource(BaseMasterEntity entity) {
        return entity instanceof Permission permission ? permission.getResource() : null;
    }

    private String action(BaseMasterEntity entity) {
        return entity instanceof Permission permission ? permission.getAction() : null;
    }
}
