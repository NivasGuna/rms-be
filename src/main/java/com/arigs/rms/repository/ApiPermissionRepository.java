package com.arigs.rms.repository;

import com.arigs.rms.entity.ApiPermission;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for API permission mappings.
 */
public interface ApiPermissionRepository extends JpaRepository<ApiPermission, UUID> {

    @EntityGraph(attributePaths = "permission")
    List<ApiPermission> findByActiveTrue();
}
