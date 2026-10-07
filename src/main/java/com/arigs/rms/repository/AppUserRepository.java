package com.arigs.rms.repository;

import com.arigs.rms.entity.AppUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for RMS users.
 */
public interface AppUserRepository extends JpaRepository<AppUser, UUID>, JpaSpecificationExecutor<AppUser> {

    @EntityGraph(attributePaths = "roles")
    Optional<AppUser> findByUsernameAndActiveTrue(String username);

    Optional<AppUser> findByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<AppUser> findByIdAndActiveTrue(UUID id);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmployeeCodeIgnoreCase(String employeeCode);
}
