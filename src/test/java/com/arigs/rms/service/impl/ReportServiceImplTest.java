package com.arigs.rms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.arigs.rms.reporting.ReportDataProvider;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReportServiceImplTest {

    @Test
    void createsService() {
        assertNotNull(new ReportServiceImpl(List.<ReportDataProvider>of()));
    }
}
