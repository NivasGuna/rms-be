package com.arigs.rms.repository;

import com.arigs.rms.entity.BaseAuditableEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository for audited RMS aggregate roots.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseAuditableEntity> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {

    Optional<T> findByIdAndActiveTrueAndDeletedFalse(UUID id);
}
