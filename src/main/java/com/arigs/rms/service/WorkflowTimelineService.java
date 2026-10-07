package com.arigs.rms.service;

import com.arigs.rms.dto.response.WorkflowTimelineResponse;
import java.util.List;
import java.util.UUID;

/**
 * Workflow timeline query use cases.
 */
public interface WorkflowTimelineService {

    List<WorkflowTimelineResponse> jobTimeline(UUID jobRequestId);

    List<WorkflowTimelineResponse> candidateTimeline(UUID candidateId);
}
