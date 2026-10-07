package com.arigs.rms.security;

import jakarta.servlet.http.HttpServletRequest;

/**
 * API permission authorization helper.
 */
public interface ApiPermissionService {

    boolean isAllowed(HttpServletRequest request);
}
