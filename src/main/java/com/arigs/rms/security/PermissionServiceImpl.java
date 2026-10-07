package com.arigs.rms.security;

import com.arigs.rms.entity.Role;
import com.arigs.rms.repository.RolePermissionRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Database-backed permission cache for role permission checks.
 */
@Service("permissionService")
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Cacheable(cacheNames = "rolePermissions", key = "#role.name()")
    @Transactional(readOnly = true)
    public Set<String> permissionsFor(Role role) {
        return rolePermissionRepository.findByRoleAndActiveTrue(role).stream()
                .filter(mapping -> mapping.getPermission().isActive() && !mapping.getPermission().isDeleted())
                .map(mapping -> mapping.getPermission().getCode())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean currentUserHasPermission(String permissionCode) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }
        return principal.getRoles().stream()
                .flatMap(role -> permissionsFor(role).stream())
                .anyMatch(permissionCode::equalsIgnoreCase);
    }

    @Override
    @CacheEvict(cacheNames = {"rolePermissions", "apiPermissions"}, allEntries = true)
    public void evictPermissionCaches() {
        // Method body intentionally empty; annotation performs cache eviction.
    }
}
