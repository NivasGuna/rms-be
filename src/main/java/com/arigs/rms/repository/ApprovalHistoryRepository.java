package com.arigs.rms.repository;

import com.arigs.rms.entity.ApprovalHistory;
import java.util.List;
import java.util.UUID;

/**
 * Repository for approval decision history.
 */
public interface ApprovalHistoryRepository extends BaseRepository<ApprovalHistory> {

    List<ApprovalHistory> findByEntityTypeAndEntityIdAndActiveTrueAndDeletedFalseOrderByCreatedDateAsc(String entityType, UUID entityId);
}
