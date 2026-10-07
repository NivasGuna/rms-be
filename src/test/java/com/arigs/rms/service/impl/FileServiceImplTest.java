package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.mapper.FileMetadataMapper;
import com.arigs.rms.repository.FileMetadataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Mock
    private FileMetadataMapper mapper;

    @InjectMocks
    private FileServiceImpl service;

    @Test
    void createsService() {
        assertNotNull(service);
    }
}
