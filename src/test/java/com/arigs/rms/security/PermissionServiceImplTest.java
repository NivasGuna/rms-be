package com.arigs.rms.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.repository.RolePermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private PermissionServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
