package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.repository.BusinessRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BusinessRuleServiceImplTest {

    @Mock
    private BusinessRuleRepository businessRuleRepository;

    @InjectMocks
    private BusinessRuleServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
