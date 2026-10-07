package com.arigs.rms.repository;

import com.arigs.rms.entity.BaseMasterEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository contract for master data tables.
 */
@NoRepositoryBean
public interface BaseMasterRepository<T extends BaseMasterEntity> extends JpaRepository<T, UUID> {

    Optional<T> findByCodeIgnoreCaseAndActiveTrue(String code);

    boolean existsByCodeIgnoreCase(String code);

    List<T> findByActiveTrueOrderBySortOrderAscNameAsc();
}
