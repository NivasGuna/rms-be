package com.arigs.rms.reporting;

import com.arigs.rms.dto.request.ReportRequest;
import com.arigs.rms.service.DashboardService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Report provider for recruiter performance.
 */
@Component
@RequiredArgsConstructor
public class DashboardReportDataProvider implements ReportDataProvider {

    private final DashboardService dashboardService;

    @Override
    public String reportCode() {
        return "RECRUITER_PERFORMANCE";
    }

    @Override
    public List<Map<String, Object>> rows(ReportRequest request) {
        return dashboardService.recruiterPerformance().stream()
                .map(row -> Map.<String, Object>of(
                        "recruiterId", row.recruiterId(),
                        "recruiterName", row.recruiterName(),
                        "submittedCandidates", row.submittedCandidates(),
                        "joinedCandidates", row.joinedCandidates()))
                .toList();
    }
}
