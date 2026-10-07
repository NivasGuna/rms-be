package com.arigs.rms.security;

import com.arigs.rms.repository.ApiPermissionRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

/**
 * Database-backed API permission matcher.
 */
@Service
@RequiredArgsConstructor
public class ApiPermissionServiceImpl implements ApiPermissionService {

    private final ApiPermissionRepository apiPermissionRepository;
    private final PermissionService permissionService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean isAllowed(HttpServletRequest request) {
        return apiPermissions().stream()
                .filter(permission -> permission.httpMethod().equalsIgnoreCase(request.getMethod()))
                .filter(permission -> pathMatcher.match(permission.pathPattern(), request.getRequestURI()))
                .findFirst()
                .map(permission -> permissionService.currentUserHasPermission(permission.permissionCode()))
                .orElse(true);
    }

    @Cacheable(cacheNames = "apiPermissions")
    public List<ApiPermissionRule> apiPermissions() {
        return apiPermissionRepository.findByActiveTrue().stream()
                .filter(rule -> !rule.isDeleted() && rule.getPermission().isActive() && !rule.getPermission().isDeleted())
                .map(rule -> new ApiPermissionRule(rule.getHttpMethod(), rule.getPathPattern(), rule.getPermission().getCode()))
                .toList();
    }

    public record ApiPermissionRule(String httpMethod, String pathPattern, String permissionCode) {
    }
}
