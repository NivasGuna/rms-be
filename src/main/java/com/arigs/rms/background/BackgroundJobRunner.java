package com.arigs.rms.background;

import com.arigs.rms.entity.BackgroundJobDefinition;
import com.arigs.rms.entity.BackgroundJobExecution;
import com.arigs.rms.entity.BackgroundJobStatus;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.repository.BackgroundJobDefinitionRepository;
import com.arigs.rms.repository.BackgroundJobExecutionRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Runs configured background jobs and records execution history.
 */
@Service
@RequiredArgsConstructor
public class BackgroundJobRunner {

    private final BackgroundJobDefinitionRepository definitionRepository;
    private final BackgroundJobExecutionRepository executionRepository;
    private final List<BackgroundJobHandler> handlers;

    @Transactional
    public BackgroundJobExecution run(String jobCode) {
        BackgroundJobDefinition definition = definitionRepository.findByCodeIgnoreCaseAndActiveTrue(jobCode)
                .orElseThrow(() -> new BusinessException("Background job definition not found"));
        BackgroundJobHandler handler = handlers.stream()
                .filter(candidate -> candidate.handlerName().equals(definition.getHandlerName()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Background job handler not found"));
        BackgroundJobExecution execution = new BackgroundJobExecution();
        execution.setJobDefinition(definition);
        execution.setStatus(BackgroundJobStatus.RUNNING);
        execution.setStartedAt(Instant.now());
        executionRepository.save(execution);
        try {
            handler.execute();
            execution.setStatus(BackgroundJobStatus.COMPLETED);
        } catch (RuntimeException ex) {
            execution.setStatus(BackgroundJobStatus.FAILED);
            execution.setFailureReason(ex.getMessage());
            throw ex;
        } finally {
            execution.setFinishedAt(Instant.now());
        }
        return execution;
    }
}
