package com.arigs.rms.repository;

import com.arigs.rms.entity.ApprovalMatrix;
import java.util.List;
import java.util.UUID;

/**
 * Repository for approval routing rules.
 */
public interface ApprovalMatrixRepository extends BaseRepository<ApprovalMatrix> {

    List<ApprovalMatrix> findByWorkflowStepIdAndActiveTrueAndDeletedFalseOrderByApprovalLevelAsc(UUID workflowStepId);
}
