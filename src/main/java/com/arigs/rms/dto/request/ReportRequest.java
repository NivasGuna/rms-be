package com.arigs.rms.dto.request;

import com.arigs.rms.reporting.ReportFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Generic report export request.
 */
public record ReportRequest(
        @NotBlank String reportCode,
        @NotNull ReportFormat format,
        LocalDate from,
        LocalDate to,
        @NotEmpty List<String> columns,
        Map<String, String> filters
) {
}
