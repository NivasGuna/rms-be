package com.arigs.rms.notification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.email.EmailService;
import com.arigs.rms.repository.NotificationLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
