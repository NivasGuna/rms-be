package com.arigs.rms.repository;

import com.arigs.rms.entity.Interview;
import com.arigs.rms.repository.projection.StatusCountProjection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for interviews.
 */
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    List<Interview> findByCandidateIdAndActiveTrueOrderByScheduledAtAsc(UUID candidateId);

    long countByActiveTrueAndDeletedFalse();

    @Query("""
            select interview.result as status, count(interview) as total
            from Interview interview
            where interview.active = true and interview.deleted = false
            group by interview.result
            """)
    List<StatusCountProjection> countByResult();
}
