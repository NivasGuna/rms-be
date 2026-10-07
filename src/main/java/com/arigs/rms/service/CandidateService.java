package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.CandidateCreateRequest;
import com.arigs.rms.dto.request.CandidateStatusUpdateRequest;
import com.arigs.rms.dto.request.InterviewRequest;
import com.arigs.rms.dto.request.JoiningRequest;
import com.arigs.rms.dto.request.OfferRequest;
import com.arigs.rms.dto.response.CandidateResponse;
import com.arigs.rms.dto.response.InterviewResponse;
import com.arigs.rms.dto.response.JoiningResponse;
import com.arigs.rms.dto.response.OfferResponse;
import com.arigs.rms.dto.response.StatusHistoryResponse;
import com.arigs.rms.entity.CandidateStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Candidate workflow use cases.
 */
public interface CandidateService {

    CandidateResponse create(CandidateCreateRequest request);

    CandidateResponse updateStatus(UUID id, CandidateStatusUpdateRequest request);

    InterviewResponse scheduleInterview(UUID candidateId, InterviewRequest request);

    OfferResponse releaseOffer(UUID candidateId, OfferRequest request);

    JoiningResponse confirmJoining(UUID candidateId, JoiningRequest request);

    CandidateResponse get(UUID id);

    PageResponse<CandidateResponse> search(UUID jobRequestId, CandidateStatus status, String keyword, Pageable pageable);

    List<StatusHistoryResponse> history(UUID id);

    List<InterviewResponse> interviews(UUID candidateId);
}
