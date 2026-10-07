package com.arigs.rms.service.impl;

import com.arigs.rms.dto.request.BusinessRuleEvaluationRequest;
import com.arigs.rms.dto.response.BusinessRuleEvaluationResponse;
import com.arigs.rms.entity.BusinessRule;
import com.arigs.rms.repository.BusinessRuleRepository;
import com.arigs.rms.service.BusinessRuleService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Database-backed business rule evaluator for scalar facts.
 */
@Service
@RequiredArgsConstructor
public class BusinessRuleServiceImpl implements BusinessRuleService {

    private final BusinessRuleRepository businessRuleRepository;

    @Override
    @Transactional(readOnly = true)
    public BusinessRuleEvaluationResponse evaluate(BusinessRuleEvaluationRequest request) {
        List<String> violations = new ArrayList<>();
        for (BusinessRule rule : businessRuleRepository.findByContextAndActiveTrueAndDeletedFalseOrderBySortOrderAscNameAsc(request.context())) {
            String actual = request.facts().get(rule.getFieldName());
            if (!matches(rule, actual)) {
                violations.add(rule.getFailureMessage());
            }
        }
        return new BusinessRuleEvaluationResponse(request.context(), violations.isEmpty(), violations);
    }

    private boolean matches(BusinessRule rule, String actual) {
        if (actual == null) {
            return false;
        }
        String expected = rule.getExpectedValue();
        return switch (rule.getOperator().toUpperCase(Locale.ROOT)) {
            case "EQUALS" -> actual.equalsIgnoreCase(expected);
            case "NOT_EQUALS" -> !actual.equalsIgnoreCase(expected);
            case "CONTAINS" -> actual.toLowerCase(Locale.ROOT).contains(expected.toLowerCase(Locale.ROOT));
            case "IN" -> List.of(expected.split(",")).stream().map(String::trim).anyMatch(actual::equalsIgnoreCase);
            case "GREATER_THAN" -> decimal(actual).compareTo(decimal(expected)) > 0;
            case "LESS_THAN" -> decimal(actual).compareTo(decimal(expected)) < 0;
            case "GREATER_THAN_OR_EQUAL" -> decimal(actual).compareTo(decimal(expected)) >= 0;
            case "LESS_THAN_OR_EQUAL" -> decimal(actual).compareTo(decimal(expected)) <= 0;
            default -> false;
        };
    }

    private BigDecimal decimal(String value) {
        return new BigDecimal(value.trim());
    }
}
