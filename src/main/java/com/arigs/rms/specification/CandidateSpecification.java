package com.arigs.rms.specification;

import com.arigs.rms.entity.Candidate;
import com.arigs.rms.entity.CandidateStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Dynamic candidate search predicates.
 */
public final class CandidateSpecification {

    private CandidateSpecification() {
    }

    public static Specification<Candidate> search(UUID jobRequestId, CandidateStatus status, String keyword) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.isTrue(root.get("active")));
            if (jobRequestId != null) {
                predicates.add(cb.equal(root.get("jobRequest").get("id"), jobRequestId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (StringUtils.hasText(keyword)) {
                String like = "%" + keyword.toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("candidateCode")), like),
                        cb.like(cb.lower(root.get("firstName")), like),
                        cb.like(cb.lower(root.get("lastName")), like),
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(cb.lower(root.get("phone")), like)));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
