package com.arigs.rms.repository;

import com.arigs.rms.entity.Candidate;
import com.arigs.rms.entity.CandidateStatus;
import com.arigs.rms.repository.projection.RecruiterPerformanceProjection;
import com.arigs.rms.repository.projection.StatusCountProjection;
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
 * Repository for candidates.
 */
public interface CandidateRepository extends JpaRepository<Candidate, UUID>, JpaSpecificationExecutor<Candidate> {

    boolean existsByCandidateCodeIgnoreCase(String candidateCode);

    @EntityGraph(attributePaths = {"jobRequest", "jobRequest.salesOwner", "jobRequest.tagManager", "jobRequest.tagAssociate"})
    Optional<Candidate> findWithJobRequestById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"jobRequest"})
    Page<Candidate> findAll(Specification<Candidate> specification, Pageable pageable);

    long countByStatusAndActiveTrueAndDeletedFalse(CandidateStatus status);

    long countByActiveTrueAndDeletedFalse();

    @Query("""
            select candidate.status as status, count(candidate) as total
            from Candidate candidate
            where candidate.active = true and candidate.deleted = false
            group by candidate.status
            """)
    java.util.List<StatusCountProjection> countByStatus();

    @Query("""
            select associate.id as recruiterId,
                   associate.fullName as recruiterName,
                   count(candidate) as submittedCandidates,
                   sum(case when candidate.status = com.arigs.rms.entity.CandidateStatus.JOINED then 1 else 0 end) as joinedCandidates
            from Candidate candidate
            join candidate.jobRequest job
            join job.tagAssociate associate
            where candidate.active = true and candidate.deleted = false
            group by associate.id, associate.fullName
            """)
    java.util.List<RecruiterPerformanceProjection> recruiterPerformance();

    java.util.List<Candidate> findByJobRequestIdAndActiveTrueAndDeletedFalse(UUID jobRequestId);
}
