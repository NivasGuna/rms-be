package com.arigs.rms.repository;

import com.arigs.rms.entity.BusinessRule;
import java.util.List;

/**
 * Repository for configurable business rules.
 */
public interface BusinessRuleRepository extends BaseMasterRepository<BusinessRule> {

    List<BusinessRule> findByContextAndActiveTrueAndDeletedFalseOrderBySortOrderAscNameAsc(String context);
}
