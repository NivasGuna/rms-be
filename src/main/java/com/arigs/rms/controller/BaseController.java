package com.arigs.rms.controller;

import com.arigs.rms.common.ApiResponse;
import com.arigs.rms.common.ApiResponseBuilder;
import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.GenericSearchRequest;
import com.arigs.rms.service.BaseService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Reusable CRUD controller base for simple resources.
 */
public abstract class BaseController<RQ, RS> {

    private final BaseService<RQ, RS> service;
    private final String resourceName;

    protected BaseController(BaseService<RQ, RS> service, String resourceName) {
        this.service = service;
        this.resourceName = resourceName;
    }

    public ResponseEntity<ApiResponse<RS>> create(@Valid @RequestBody RQ request) {
        return ApiResponseBuilder.created(resourceName + " created", service.create(request));
    }

    public ResponseEntity<ApiResponse<RS>> update(@PathVariable UUID id, @Valid @RequestBody RQ request) {
        return ApiResponseBuilder.ok(resourceName + " updated", service.update(id, request));
    }

    public ResponseEntity<ApiResponse<RS>> get(@PathVariable UUID id) {
        return ApiResponseBuilder.ok(resourceName + " retrieved", service.get(id));
    }

    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ApiResponseBuilder.ok(resourceName + " deleted", null);
    }

    public ResponseEntity<ApiResponse<PageResponse<RS>>> search(@Valid @RequestBody GenericSearchRequest request) {
        return ApiResponseBuilder.ok(resourceName + " search completed", service.search(request));
    }
}
