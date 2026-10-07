package com.arigs.rms.service.impl;

import com.arigs.rms.dto.request.ReportRequest;
import com.arigs.rms.dto.response.ReportExportResponse;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.reporting.ReportDataProvider;
import com.arigs.rms.reporting.ReportFormat;
import com.arigs.rms.service.ReportService;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * Default report exporter supporting CSV and XLSX.
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final List<ReportDataProvider> providers;

    @Override
    public ReportExportResponse export(ReportRequest request) {
        ReportDataProvider provider = providers.stream()
                .filter(candidate -> candidate.reportCode().equalsIgnoreCase(request.reportCode()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Report provider not found"));
        List<Map<String, Object>> rows = provider.rows(request);
        byte[] content = request.format() == ReportFormat.CSV ? csv(request.columns(), rows) : xlsx(request.columns(), rows);
        String extension = request.format() == ReportFormat.CSV ? ".csv" : ".xlsx";
        String contentType = request.format() == ReportFormat.CSV
                ? "text/csv"
                : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        return new ReportExportResponse(
                request.reportCode().toLowerCase() + extension,
                contentType,
                Base64.getEncoder().encodeToString(content));
    }

    private byte[] csv(List<String> columns, List<Map<String, Object>> rows) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.join(",", columns)).append("\n");
        for (Map<String, Object> row : rows) {
            builder.append(columns.stream()
                    .map(column -> escapeCsv(row.get(column)))
                    .reduce((left, right) -> left + "," + right)
                    .orElse(""))
                    .append("\n");
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(Object value) {
        String text = value == null ? "" : value.toString();
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }

    private byte[] xlsx(List<String> columns, List<Map<String, Object>> rows) {
        try (var workbook = new XSSFWorkbook(); var outputStream = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Report");
            var header = sheet.createRow(0);
            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                header.createCell(columnIndex).setCellValue(columns.get(columnIndex));
            }
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                var sheetRow = sheet.createRow(rowIndex + 1);
                Map<String, Object> row = rows.get(rowIndex);
                for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                    Object value = row.get(columns.get(columnIndex));
                    sheetRow.createCell(columnIndex).setCellValue(value == null ? "" : value.toString());
                }
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new BusinessException("Report export failed");
        }
    }
}
