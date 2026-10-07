package com.arigs.rms.repository;

import com.arigs.rms.entity.Offer;
import com.arigs.rms.entity.OfferStatus;
import com.arigs.rms.repository.projection.StatusCountProjection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for candidate offers.
 */
public interface OfferRepository extends JpaRepository<Offer, UUID> {

    Optional<Offer> findByCandidateIdAndActiveTrue(UUID candidateId);

    boolean existsByCandidateIdAndActiveTrue(UUID candidateId);

    long countByStatusAndActiveTrueAndDeletedFalse(OfferStatus status);

    @Query("""
            select offer.status as status, count(offer) as total
            from Offer offer
            where offer.active = true and offer.deleted = false
            group by offer.status
            """)
    java.util.List<StatusCountProjection> countByStatus();
}
