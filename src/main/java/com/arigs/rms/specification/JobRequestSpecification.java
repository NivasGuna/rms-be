package com.arigs.rms.specification;

import com.arigs.rms.entity.JobRequest;
import com.arigs.rms.entity.JobRequestStatus;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Dynamic job request search predicates.
 */
public final class JobRequestSpecification {

    private JobRequestSpecification() {
    }

    public static Specification<JobRequest> search(String keyword, JobRequestStatus status) {
        return search(keyword, status != null ? List.of(status) : null);
    }

    public static Specification<JobRequest> search(String keyword, Collection<JobRequestStatus> statuses) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.isTrue(root.get("active")));
            predicates.add(cb.isFalse(root.get("deleted")));
            if (statuses != null && !statuses.isEmpty()) {
                predicates.add(root.get("status").in(statuses));
            }
            if (StringUtils.hasText(keyword)) {
                String like = "%" + keyword.toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("requestNumber")), like),
                        cb.like(cb.lower(root.get("clientName")), like),
                        cb.like(cb.lower(root.get("projectName")), like),
                        cb.like(cb.lower(root.get("jobTitle")), like)));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
