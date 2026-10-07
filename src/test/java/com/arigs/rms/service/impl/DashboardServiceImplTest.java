package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.repository.CandidateRepository;
import com.arigs.rms.repository.InterviewRepository;
import com.arigs.rms.repository.JobRequestRepository;
import com.arigs.rms.repository.JoiningRepository;
import com.arigs.rms.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private JobRequestRepository jobRequestRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private JoiningRepository joiningRepository;

    @InjectMocks
    private DashboardServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
