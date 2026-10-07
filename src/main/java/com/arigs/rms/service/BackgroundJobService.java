package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.response.BackgroundJobExecutionResponse;
import org.springframework.data.domain.Pageable;

/**
 * Background job administration use cases.
 */
public interface BackgroundJobService {

    BackgroundJobExecutionResponse run(String jobCode);

    PageResponse<BackgroundJobExecutionResponse> executions(Pageable pageable);
}
