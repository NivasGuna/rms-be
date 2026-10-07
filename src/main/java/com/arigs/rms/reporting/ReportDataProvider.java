package com.arigs.rms.reporting;

import com.arigs.rms.dto.request.ReportRequest;
import java.util.List;
import java.util.Map;

/**
 * Supplies tabular report rows for a report code.
 */
public interface ReportDataProvider {

    String reportCode();

    List<Map<String, Object>> rows(ReportRequest request);
}
