package com.arigs.rms.repository;

import com.arigs.rms.entity.MenuPermission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for menu permission mappings.
 */
public interface MenuPermissionRepository extends JpaRepository<MenuPermission, UUID> {
}
