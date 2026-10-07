package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.AuditSearchRequest;
import com.arigs.rms.dto.response.AuditTrailResponse;

/**
 * Audit trail query use cases.
 */
public interface AuditTrailService {

    PageResponse<AuditTrailResponse> search(AuditSearchRequest request);
}
