package com.arigs.rms.entity;

/**
 * Configurable workflow states represented as stable backend statuses.
 */
public enum JobRequestStatus {
    DRAFT,
    PENDING_BUSINESS_APPROVAL,
    BUSINESS_APPROVED,
    BUSINESS_REJECTED,
    TAG_MANAGER_ASSIGNED,
    TAG_ASSOCIATE_ASSIGNED,
    CANDIDATE_SOURCING,
    INTERVIEW_IN_PROGRESS,
    OFFER_RELEASED,
    JOINED,
    CLOSED,
    CANCELLED
}
