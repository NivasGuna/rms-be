package com.arigs.rms.service;

import com.arigs.rms.dto.request.BusinessRuleEvaluationRequest;
import com.arigs.rms.dto.response.BusinessRuleEvaluationResponse;

/**
 * Configurable business rule evaluation use cases.
 */
public interface BusinessRuleService {

    BusinessRuleEvaluationResponse evaluate(BusinessRuleEvaluationRequest request);
}
