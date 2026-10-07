package com.arigs.rms.dto.response;

/**
 * Report export response containing encoded export bytes.
 */
public record ReportExportResponse(
        String filename,
        String contentType,
        String base64Content
) {
}
