package com.arigs.rms.repository;

import com.arigs.rms.entity.Role;
import com.arigs.rms.entity.RolePermission;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for role permission mappings.
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    @EntityGraph(attributePaths = "permission")
    List<RolePermission> findByRoleAndActiveTrue(Role role);
}
