package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.audit.AuditService;
import com.arigs.rms.repository.ApprovalHistoryRepository;
import com.arigs.rms.repository.WorkflowActionRepository;
import com.arigs.rms.repository.WorkflowDefinitionRepository;
import com.arigs.rms.security.PermissionService;
import com.arigs.rms.service.BusinessRuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowEngineServiceImplTest {

    @Mock
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Mock
    private WorkflowActionRepository workflowActionRepository;

    @Mock
    private ApprovalHistoryRepository approvalHistoryRepository;

    @Mock
    private PermissionService permissionService;

    @Mock
    private BusinessRuleService businessRuleService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private WorkflowEngineServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
