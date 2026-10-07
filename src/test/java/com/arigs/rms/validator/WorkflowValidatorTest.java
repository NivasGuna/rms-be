package com.arigs.rms.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.arigs.rms.entity.CandidateStatus;
import com.arigs.rms.entity.JobRequestStatus;
import com.arigs.rms.exception.BusinessException;
import org.junit.jupiter.api.Test;

class WorkflowValidatorTest {

    private final WorkflowValidator validator = new WorkflowValidator();

    @Test
    void allowsConfiguredJobTransition() {
        assertDoesNotThrow(() -> validator.validateJobTransition(
                JobRequestStatus.PENDING_BUSINESS_APPROVAL,
                JobRequestStatus.BUSINESS_APPROVED));
    }

    @Test
    void rejectsInvalidCandidateTransition() {
        assertThrows(BusinessException.class, () -> validator.validateCandidateTransition(
                CandidateStatus.UPLOADED,
                CandidateStatus.JOINED));
    }
}
