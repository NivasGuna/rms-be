package com.arigs.rms.repository;

import com.arigs.rms.entity.JobRequest;
import com.arigs.rms.entity.JobRequestStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for job requests.
 */
public interface JobRequestRepository extends JpaRepository<JobRequest, UUID>, JpaSpecificationExecutor<JobRequest> {

    boolean existsByRequestNumberIgnoreCase(String requestNumber);

    @EntityGraph(attributePaths = {"salesOwner", "businessApprover", "tagManager", "tagAssociate"})
    Optional<JobRequest> findWithAssignmentsById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"salesOwner", "businessApprover", "tagManager", "tagAssociate"})
    Page<JobRequest> findAll(Specification<JobRequest> specification, Pageable pageable);

    long countByActiveTrueAndDeletedFalse();

    long countByStatusAndActiveTrueAndDeletedFalse(JobRequestStatus status);

    long countByStatusInAndActiveTrueAndDeletedFalse(java.util.Collection<JobRequestStatus> statuses);

    @Query("""
            select coalesce(sum(job.numberOfPositions), 0)
            from JobRequest job
            where job.active = true
              and job.deleted = false
              and job.status not in (com.arigs.rms.entity.JobRequestStatus.CLOSED, com.arigs.rms.entity.JobRequestStatus.CANCELLED)
            """)
    long sumOpenPositions();
}
