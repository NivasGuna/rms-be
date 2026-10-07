package com.arigs.rms.service;

import com.arigs.rms.dto.request.ReportRequest;
import com.arigs.rms.dto.response.ReportExportResponse;

/**
 * Report export use cases.
 */
public interface ReportService {

    ReportExportResponse export(ReportRequest request);
}
