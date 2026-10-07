package com.arigs.rms.mapper;

import com.arigs.rms.dto.response.CandidateResponse;
import com.arigs.rms.dto.response.InterviewResponse;
import com.arigs.rms.dto.response.JoiningResponse;
import com.arigs.rms.dto.response.OfferResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.Candidate;
import com.arigs.rms.entity.CandidateStatusHistory;
import com.arigs.rms.entity.Interview;
import com.arigs.rms.entity.Joining;
import com.arigs.rms.entity.Offer;
import org.mapstruct.Mapper;

/**
 * Maps candidate workflow entities to API DTOs.
 */
@Mapper(componentModel = "spring")
public interface CandidateMapper {

    default CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getJobRequest().getId(),
                candidate.getJobRequest().getRequestNumber(),
                candidate.getCandidateCode(),
                candidate.getFirstName(),
                candidate.getLastName(),
                candidate.getEmail(),
                candidate.getPhone(),
                candidate.getCurrentCompany(),
                candidate.getTotalExperienceYears(),
                candidate.getExpectedCtc(),
                candidate.getCurrentCtc(),
                candidate.getNoticePeriodDays(),
                candidate.getStatus(),
                candidate.isActive());
    }

    default StatusHistoryResponse toHistoryResponse(CandidateStatusHistory history) {
        return new StatusHistoryResponse(
                history.getId(),
                history.getOldStatus() == null ? null : history.getOldStatus().name(),
                history.getNewStatus().name(),
                history.getComments(),
                history.getCreatedBy(),
                history.getCreatedDate());
    }

    default InterviewResponse toInterviewResponse(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getCandidate().getId(),
                interview.getRoundName(),
                interview.getScheduledAt(),
                interview.getMode(),
                interview.getInterviewerName(),
                interview.getFeedback(),
                interview.getResult());
    }

    default OfferResponse toOfferResponse(Offer offer) {
        return new OfferResponse(
                offer.getId(),
                offer.getCandidate().getId(),
                offer.getOfferAmount(),
                offer.getJoiningDate(),
                offer.getStatus());
    }

    default JoiningResponse toJoiningResponse(Joining joining) {
        return new JoiningResponse(
                joining.getId(),
                joining.getCandidate().getId(),
                joining.getJoiningDate(),
                joining.getStatus(),
                joining.getRemarks());
    }
}
