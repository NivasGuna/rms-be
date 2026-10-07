package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.mapper.JobRequestMapper;
import com.arigs.rms.repository.AppUserRepository;
import com.arigs.rms.repository.JobRequestRepository;
import com.arigs.rms.repository.JobStatusHistoryRepository;
import com.arigs.rms.validator.WorkflowValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobRequestServiceImplTest {

    @Mock
    private JobRequestRepository jobRequestRepository;

    @Mock
    private JobStatusHistoryRepository historyRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private JobRequestMapper mapper;

    @Mock
    private WorkflowValidator workflowValidator;

    @InjectMocks
    private JobRequestServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
