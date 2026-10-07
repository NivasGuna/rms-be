package com.arigs.rms.service;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.GenericSearchRequest;
import java.util.UUID;

/**
 * Reusable CRUD service contract.
 */
public interface BaseService<RQ, RS> {

    RS create(RQ request);

    RS update(UUID id, RQ request);

    RS get(UUID id);

    void delete(UUID id);

    PageResponse<RS> search(GenericSearchRequest request);
}
