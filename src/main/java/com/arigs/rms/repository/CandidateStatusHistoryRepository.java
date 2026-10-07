package com.arigs.rms.repository;

import com.arigs.rms.entity.CandidateStatusHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for candidate status history.
 */
public interface CandidateStatusHistoryRepository extends JpaRepository<CandidateStatusHistory, UUID> {

    List<CandidateStatusHistory> findByCandidateIdOrderByCreatedDateAsc(UUID candidateId);
}
