package com.arigs.rms.service;

import com.arigs.rms.dto.request.WorkflowTransitionRequest;
import com.arigs.rms.dto.response.WorkflowTransitionResponse;

/**
 * Configurable workflow execution use cases.
 */
public interface WorkflowEngineService {

    WorkflowTransitionResponse transition(WorkflowTransitionRequest request);
}
