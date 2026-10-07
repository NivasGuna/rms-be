package com.arigs.rms.repository;

import com.arigs.rms.entity.Joining;
import com.arigs.rms.entity.JoiningStatus;
import com.arigs.rms.repository.projection.MonthlyCountProjection;
import com.arigs.rms.repository.projection.StatusCountProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for candidate joining records.
 */
public interface JoiningRepository extends JpaRepository<Joining, UUID> {

    Optional<Joining> findByCandidateIdAndActiveTrue(UUID candidateId);

    boolean existsByCandidateIdAndActiveTrue(UUID candidateId);

    long countByStatusAndActiveTrueAndDeletedFalse(JoiningStatus status);

    @Query("""
            select joining.status as status, count(joining) as total
            from Joining joining
            where joining.active = true and joining.deleted = false
            group by joining.status
            """)
    List<StatusCountProjection> countByStatus();

    @Query("""
            select year(joining.joiningDate) as year, month(joining.joiningDate) as month, count(joining) as total
            from Joining joining
            where joining.active = true
              and joining.deleted = false
              and joining.status = com.arigs.rms.entity.JoiningStatus.JOINED
              and joining.joiningDate between :from and :to
            group by year(joining.joiningDate), month(joining.joiningDate)
            order by year(joining.joiningDate), month(joining.joiningDate)
            """)
    List<MonthlyCountProjection> monthlyHiring(LocalDate from, LocalDate to);
}
