package com.arigs.rms.service.impl;

import com.arigs.rms.background.BackgroundJobRunner;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.response.BackgroundJobExecutionResponse;
import com.arigs.rms.entity.BackgroundJobExecution;
import com.arigs.rms.repository.BackgroundJobExecutionRepository;
import com.arigs.rms.service.BackgroundJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default background job administration service.
 */
@Service
@RequiredArgsConstructor
public class BackgroundJobServiceImpl implements BackgroundJobService {

    private final BackgroundJobRunner backgroundJobRunner;
    private final BackgroundJobExecutionRepository executionRepository;

    @Override
    @Transactional
    public BackgroundJobExecutionResponse run(String jobCode) {
        return toResponse(backgroundJobRunner.run(jobCode));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BackgroundJobExecutionResponse> executions(Pageable pageable) {
        return PageResponse.from(executionRepository
                .findByActiveTrueAndDeletedFalseOrderByCreatedDateDesc(pageable)
                .map(this::toResponse));
    }

    private BackgroundJobExecutionResponse toResponse(BackgroundJobExecution execution) {
        return new BackgroundJobExecutionResponse(
                execution.getId(),
                execution.getJobDefinition().getCode(),
                execution.getJobDefinition().getHandlerName(),
                execution.getStatus(),
                execution.getStartedAt(),
                execution.getFinishedAt(),
                execution.getFailureReason());
    }
}
