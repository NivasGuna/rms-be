package com.arigs.rms.service.impl;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.AuditSearchRequest;
import com.arigs.rms.dto.response.AuditTrailResponse;
import com.arigs.rms.entity.AuditLog;
import com.arigs.rms.mapper.AuditMapper;
import com.arigs.rms.repository.AuditLogRepository;
import com.arigs.rms.service.AuditTrailService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default audit trail query service.
 */
@Service
@RequiredArgsConstructor
public class AuditTrailServiceImpl implements AuditTrailService {

    private final AuditLogRepository auditLogRepository;
    private final AuditMapper auditMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditTrailResponse> search(AuditSearchRequest request) {
        AuditSearchRequest safeRequest = request == null
                ? new AuditSearchRequest(null, null, null, null, null, null, null, 0, 50)
                : request;
        PageRequest pageable = PageRequest.of(
                safeRequest.pageOrDefault(),
                safeRequest.sizeOrDefault(),
                Sort.by(Sort.Direction.DESC, "createdDate"));
        return PageResponse.from(auditLogRepository.findAll(specification(safeRequest), pageable).map(auditMapper::toResponse));
    }

    private Specification<AuditLog> specification(AuditSearchRequest request) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.isTrue(root.get("active")));
            predicates.add(cb.isFalse(root.get("deleted")));
            if (request.eventType() != null) {
                predicates.add(cb.equal(root.get("eventType"), request.eventType()));
            }
            if (StringUtils.hasText(request.actor())) {
                predicates.add(cb.equal(cb.lower(root.get("actor")), request.actor().toLowerCase()));
            }
            if (StringUtils.hasText(request.action())) {
                predicates.add(cb.equal(cb.lower(root.get("action")), request.action().toLowerCase()));
            }
            if (StringUtils.hasText(request.entityType())) {
                predicates.add(cb.equal(cb.lower(root.get("entityType")), request.entityType().toLowerCase()));
            }
            if (StringUtils.hasText(request.entityId())) {
                predicates.add(cb.equal(root.get("entityId"), request.entityId()));
            }
            if (request.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), request.from()));
            }
            if (request.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), request.to()));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
