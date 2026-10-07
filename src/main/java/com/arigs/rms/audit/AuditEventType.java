package com.arigs.rms.audit;

/**
 * High-level auditable event categories.
 */
public enum AuditEventType {
    AUTHENTICATION,
    USER_ADMINISTRATION,
    JOB_WORKFLOW,
    CANDIDATE_WORKFLOW,
    INTERVIEW_WORKFLOW,
    OFFER_WORKFLOW,
    JOINING_WORKFLOW,
    APPROVAL,
    ASSIGNMENT,
    FILE_UPLOAD,
    FILE_DOWNLOAD,
    NOTIFICATION,
    SYSTEM
}
