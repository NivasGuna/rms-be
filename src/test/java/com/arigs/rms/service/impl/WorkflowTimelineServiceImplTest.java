package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.repository.CandidateRepository;
import com.arigs.rms.repository.CandidateStatusHistoryRepository;
import com.arigs.rms.repository.InterviewRepository;
import com.arigs.rms.repository.JobStatusHistoryRepository;
import com.arigs.rms.repository.JoiningRepository;
import com.arigs.rms.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowTimelineServiceImplTest {

    @Mock
    private JobStatusHistoryRepository jobStatusHistoryRepository;

    @Mock
    private CandidateStatusHistoryRepository candidateStatusHistoryRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private JoiningRepository joiningRepository;

    @InjectMocks
    private WorkflowTimelineServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
