package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.mapper.UserMapper;
import com.arigs.rms.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
