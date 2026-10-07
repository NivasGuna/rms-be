package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.repository.AppUserRepository;
import com.arigs.rms.repository.InAppNotificationRepository;
import com.arigs.rms.repository.NotificationPreferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationCenterServiceImplTest {

    @Mock
    private InAppNotificationRepository notificationRepository;

    @Mock
    private NotificationPreferenceRepository preferenceRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private NotificationCenterServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
