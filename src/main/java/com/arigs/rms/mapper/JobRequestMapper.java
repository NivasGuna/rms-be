package com.arigs.rms.mapper;

import com.arigs.rms.dto.response.JobRequestResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.AppUser;
import com.arigs.rms.entity.JobRequest;
import com.arigs.rms.entity.JobStatusHistory;
import java.util.UUID;
import org.mapstruct.Mapper;

/**
 * Maps job workflow entities to API DTOs.
 */
@Mapper(componentModel = "spring")
public interface JobRequestMapper {

    default JobRequestResponse toResponse(JobRequest jobRequest) {
        return new JobRequestResponse(
                jobRequest.getId(),
                jobRequest.getRequestNumber(),
                jobRequest.getClientName(),
                jobRequest.getProjectName(),
                jobRequest.getJobTitle(),
                jobRequest.getDescription(),
                jobRequest.getLocation(),
                jobRequest.getEmploymentType(),
                jobRequest.getPriority(),
                jobRequest.getNumberOfPositions(),
                jobRequest.getMinExperienceYears(),
                jobRequest.getMaxExperienceYears(),
                jobRequest.getBudgetAmount(),
                jobRequest.getTargetDate(),
                jobRequest.getStatus(),
                id(jobRequest.getSalesOwner()),
                name(jobRequest.getSalesOwner()),
                id(jobRequest.getBusinessApprover()),
                name(jobRequest.getBusinessApprover()),
                id(jobRequest.getTagManager()),
                name(jobRequest.getTagManager()),
                id(jobRequest.getTagAssociate()),
                name(jobRequest.getTagAssociate()),
                jobRequest.isActive());
    }

    default StatusHistoryResponse toHistoryResponse(JobStatusHistory history) {
        return new StatusHistoryResponse(
                history.getId(),
                history.getOldStatus() == null ? null : history.getOldStatus().name(),
                history.getNewStatus().name(),
                history.getComments(),
                history.getCreatedBy(),
                history.getCreatedDate());
    }

    private UUID id(AppUser user) {
        return user == null ? null : user.getId();
    }

    private String name(AppUser user) {
        return user == null ? null : user.getFullName();
    }
}
