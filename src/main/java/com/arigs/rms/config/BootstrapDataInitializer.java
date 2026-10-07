package com.arigs.rms.config;

import com.arigs.rms.entity.AppUser;
import com.arigs.rms.entity.Role;
import com.arigs.rms.repository.AppUserRepository;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds the first administrator account for a fresh installation.
 */
@Component
@RequiredArgsConstructor
public class BootstrapDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(BootstrapDataInitializer.class);

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@arigs.com";
    private static final String ADMIN_PASSWORD = "Admin@123";
    private static final String ADMIN_EMPLOYEE_CODE = "ADMIN";
    private static final String ADMIN_FULL_NAME = "Bootstrap Administrator";
    private static final String ADMIN_ROLE_NAME = "ADMINISTRATOR";

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Role administratorRole = administratorRole();

        if (userRepository.existsByUsernameIgnoreCase(ADMIN_USERNAME) || userRepository.count() > 0) {
            log.info("Bootstrap administrator already exists.");
            return;
        }

        Instant now = Instant.now();

        AppUser user = new AppUser();
        user.setUsername(ADMIN_USERNAME);
        user.setEmail(ADMIN_EMAIL);
        user.setEmployeeCode(ADMIN_EMPLOYEE_CODE);
        user.setFullName(ADMIN_FULL_NAME);
        user.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setPasswordChangedAt(now);
        user.setPasswordExpiresAt(null);
        user.setActive(true);
        user.setDeleted(false);
        user.setRoles(new LinkedHashSet<>(Set.of(administratorRole)));

        userRepository.save(user);
        log.info("Bootstrap administrator created successfully.");
    }

    private Role administratorRole() {
        try {
            return Role.valueOf(ADMIN_ROLE_NAME);
        } catch (IllegalArgumentException ex) {
            log.error("Required bootstrap role {} does not exist.", ADMIN_ROLE_NAME);
            throw new IllegalStateException("Required bootstrap role ADMINISTRATOR does not exist.", ex);
        }
    }
}
