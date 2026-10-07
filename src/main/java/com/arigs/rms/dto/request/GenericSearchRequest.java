package com.arigs.rms.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import java.util.List;

/**
 * Generic paginated search request.
 */
public record GenericSearchRequest(
        String keyword,
        @Valid List<SearchFilter> filters,
        Instant createdFrom,
        Instant createdTo,
        @Min(0) Integer page,
        @Min(1) @Max(200) Integer size,
        String sortBy,
        String sortDirection
) {
    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 20 : size;
    }

    public String sortByOrDefault() {
        return sortBy == null || sortBy.isBlank() ? "createdDate" : sortBy;
    }

    public String sortDirectionOrDefault() {
        return sortDirection == null || sortDirection.isBlank() ? "DESC" : sortDirection;
    }
}
