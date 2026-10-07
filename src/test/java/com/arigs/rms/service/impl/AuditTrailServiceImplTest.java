package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.mapper.AuditMapper;
import com.arigs.rms.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuditTrailServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditTrailServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
