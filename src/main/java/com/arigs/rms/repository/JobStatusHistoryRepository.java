package com.arigs.rms.repository;

import com.arigs.rms.entity.JobStatusHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for job request status history.
 */
public interface JobStatusHistoryRepository extends JpaRepository<JobStatusHistory, UUID> {

    List<JobStatusHistory> findByJobRequestIdOrderByCreatedDateAsc(UUID jobRequestId);
}
