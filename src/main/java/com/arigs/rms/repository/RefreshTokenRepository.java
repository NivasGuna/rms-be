package com.arigs.rms.repository;

import com.arigs.rms.entity.RefreshToken;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for refresh tokens.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHashAndActiveTrue(String tokenHash);

    @Modifying
    @Query("update RefreshToken token set token.revokedAt = :revokedAt where token.user.id = :userId and token.revokedAt is null")
    int revokeActiveTokensForUser(UUID userId, Instant revokedAt);
}
