package com.arigs.rms.security;

import com.arigs.rms.entity.Role;
import java.util.Set;

/**
 * Permission lookup and authorization helper.
 */
public interface PermissionService {

    Set<String> permissionsFor(Role role);

    boolean currentUserHasPermission(String permissionCode);

    void evictPermissionCaches();
}
