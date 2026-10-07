package com.arigs.rms.mapper;

/**
 * Generic mapper contract for reusable CRUD services.
 */
public interface GenericMapper<E, RQ, RS> {

    E toEntity(RQ request);

    void updateEntity(RQ request, E entity);

    RS toResponse(E entity);
}
