package com.arigs.rms.repository;

import com.arigs.rms.entity.Permission;
import java.util.Collection;
import java.util.List;

/**
 * Repository for fine-grained permissions.
 */
public interface PermissionRepository extends BaseMasterRepository<Permission> {

    List<Permission> findByCodeInAndActiveTrue(Collection<String> codes);
}
