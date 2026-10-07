package com.arigs.rms.specification;

import com.arigs.rms.dto.request.GenericSearchRequest;
import com.arigs.rms.dto.request.SearchFilter;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Builds dynamic specifications for simple scalar entity fields.
 */
@Component
public class GenericSpecificationBuilder<T> {

    public Specification<T> build(GenericSearchRequest request, List<String> keywordFields) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));
            predicates.add(cb.isFalse(root.get("deleted")));

            if (request != null && request.createdFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), request.createdFrom()));
            }
            if (request != null && request.createdTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), request.createdTo()));
            }
            if (request != null && StringUtils.hasText(request.keyword()) && !keywordFields.isEmpty()) {
                String like = "%" + request.keyword().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(keywordFields.stream()
                        .map(field -> cb.like(cb.lower(root.get(field)), like))
                        .toArray(Predicate[]::new)));
            }
            if (request != null && request.filters() != null) {
                request.filters().forEach(filter -> predicates.add(predicateFor(filter, root.get(filter.field()), cb)));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate predicateFor(SearchFilter filter, Path<?> path, jakarta.persistence.criteria.CriteriaBuilder cb) {
        List<String> values = filter.values() == null ? List.of() : filter.values();
        return switch (filter.operator()) {
            case EQUALS -> cb.equal(path, first(values));
            case NOT_EQUALS -> cb.notEqual(path, first(values));
            case CONTAINS -> cb.like(cb.lower(path.as(String.class)), "%" + first(values).toLowerCase(Locale.ROOT) + "%");
            case STARTS_WITH -> cb.like(cb.lower(path.as(String.class)), first(values).toLowerCase(Locale.ROOT) + "%");
            case IN -> path.in(values);
            case GREATER_THAN -> cb.greaterThan(path.as(String.class), first(values));
            case LESS_THAN -> cb.lessThan(path.as(String.class), first(values));
            case BETWEEN -> cb.between(
                    path.as(String.class),
                    first(values),
                    values.size() > 1 ? values.get(1) : first(values));   
        };
    }

    private String first(List<String> values) {
        return values.isEmpty() ? "" : values.get(0);
    }
}
