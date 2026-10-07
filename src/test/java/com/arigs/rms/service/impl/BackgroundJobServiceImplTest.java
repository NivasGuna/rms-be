package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.background.BackgroundJobRunner;
import com.arigs.rms.repository.BackgroundJobExecutionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BackgroundJobServiceImplTest {

    @Mock
    private BackgroundJobRunner backgroundJobRunner;

    @Mock
    private BackgroundJobExecutionRepository executionRepository;

    @InjectMocks
    private BackgroundJobServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
