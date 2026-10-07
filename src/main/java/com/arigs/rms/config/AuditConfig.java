package com.arigs.rms.config;

import com.arigs.rms.security.UserPrincipal;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Supplies the current authenticated user for JPA audit columns.
 */
@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(principal -> principal instanceof UserPrincipal userPrincipal
                        ? userPrincipal.getUsername()
                        : principal.toString())
                .filter(principal -> !"anonymousUser".equals(principal));
    }
}
