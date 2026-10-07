package com.arigs.rms.repository;

import com.arigs.rms.entity.BackgroundJobExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository for background job execution history.
 */
public interface BackgroundJobExecutionRepository extends BaseRepository<BackgroundJobExecution> {

    Page<BackgroundJobExecution> findByActiveTrueAndDeletedFalseOrderByCreatedDateDesc(Pageable pageable);
}
