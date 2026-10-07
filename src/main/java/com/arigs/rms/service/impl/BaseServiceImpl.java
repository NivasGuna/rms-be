package com.arigs.rms.service.impl;

import com.arigs.rms.common.PageResponse;
import com.arigs.rms.dto.request.GenericSearchRequest;
import com.arigs.rms.entity.BaseAuditableEntity;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.mapper.GenericMapper;
import com.arigs.rms.repository.BaseRepository;
import com.arigs.rms.service.BaseService;
import com.arigs.rms.specification.GenericSpecificationBuilder;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic CRUD implementation for simple aggregate roots.
 */
public abstract class BaseServiceImpl<E extends BaseAuditableEntity, RQ, RS> implements BaseService<RQ, RS> {

    private final BaseRepository<E> repository;
    private final GenericMapper<E, RQ, RS> mapper;
    private final GenericSpecificationBuilder<E> specificationBuilder;
    private final List<String> keywordFields;
    private final String entityName;

    protected BaseServiceImpl(
            BaseRepository<E> repository,
            GenericMapper<E, RQ, RS> mapper,
            GenericSpecificationBuilder<E> specificationBuilder,
            List<String> keywordFields,
            String entityName) {
        this.repository = repository;
        this.mapper = mapper;
        this.specificationBuilder = specificationBuilder;
        this.keywordFields = List.copyOf(keywordFields);
        this.entityName = entityName;
    }

    @Override
    @Transactional
    public RS create(RQ request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public RS update(UUID id, RQ request) {
        E entity = load(id);
        mapper.updateEntity(request, entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RS get(UUID id) {
        return mapper.toResponse(load(id));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        load(id).markDeleted(currentActor(), Instant.now());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RS> search(GenericSearchRequest request) {
        GenericSearchRequest safeRequest = request == null
                ? new GenericSearchRequest(null, List.of(), null, null, 0, 20, "createdDate", "DESC")
                : request;
        Sort.Direction direction = Sort.Direction.fromString(safeRequest.sortDirectionOrDefault());
        PageRequest pageable = PageRequest.of(
                safeRequest.pageOrDefault(),
                safeRequest.sizeOrDefault(),
                Sort.by(direction, safeRequest.sortByOrDefault()));
        return PageResponse.from(repository.findAll(specificationBuilder.build(safeRequest, keywordFields), pageable)
                .map(mapper::toResponse));
    }

    protected E load(UUID id) {
        return repository.findByIdAndActiveTrueAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " not found"));
    }

    private String currentActor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? "system" : authentication.getName();
    }
}
