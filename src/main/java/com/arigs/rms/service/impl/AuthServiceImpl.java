package com.arigs.rms.service.impl;

import com.arigs.rms.audit.AuditEventType;
import com.arigs.rms.audit.AuditService;
import com.arigs.rms.config.RmsProperties;
import com.arigs.rms.dto.request.LoginRequest;
import com.arigs.rms.dto.request.RefreshTokenRequest;
import com.arigs.rms.dto.response.AuthResponse;
import com.arigs.rms.entity.RefreshToken;
import com.arigs.rms.event.DomainEvent;
import com.arigs.rms.event.DomainEventPublisher;
import com.arigs.rms.exception.BusinessException;
import com.arigs.rms.exception.ResourceNotFoundException;
import com.arigs.rms.mapper.UserMapper;
import com.arigs.rms.repository.AppUserRepository;
import com.arigs.rms.repository.RefreshTokenRepository;
import com.arigs.rms.security.JwtService;
import com.arigs.rms.security.UserPrincipal;
import com.arigs.rms.service.AuthService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default authentication service.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final AuditService auditService;
    private final DomainEventPublisher domainEventPublisher;
    private final RmsProperties properties;

    @Override
    @Transactional(noRollbackFor = AuthenticationException.class)
    public AuthResponse login(LoginRequest request) {
        var authentication = authenticate(request);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        resetFailedLoginAttempts(principal.getUsername());
        IssuedRefreshToken refreshToken = issueRefreshToken(principal);
        log.info("User {} authenticated successfully", principal.getUsername());
        auditService.record(AuditEventType.AUTHENTICATION, "LOGIN", "AppUser", principal.getId().toString(), "User login successful");
        domainEventPublisher.publish(DomainEvent.of("auth.login.succeeded", "AppUser", principal.getId().toString(),
                Map.of("username", principal.getUsername())));
        return response(principal, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken currentToken = refreshTokenRepository.findByTokenHashAndActiveTrue(hash(request.refreshToken()))
                .orElseThrow(() -> new BusinessException("Refresh token is invalid"));
        if (!currentToken.isUsable(Instant.now())) {
            throw new BusinessException("Refresh token is expired or revoked");
        }
        currentToken.setRevokedAt(Instant.now());
        UserPrincipal principal = new UserPrincipal(currentToken.getUser());
        IssuedRefreshToken newToken = issueRefreshToken(principal);
        return response(principal, newToken);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByTokenHashAndActiveTrue(hash(request.refreshToken()))
                .ifPresent(token -> {
                    token.setRevokedAt(Instant.now());
                    auditService.record(AuditEventType.AUTHENTICATION, "LOGOUT", "AppUser",
                            token.getUser().getId().toString(), "Refresh token revoked");
                    domainEventPublisher.publish(DomainEvent.of("auth.logout.succeeded", "AppUser",
                            token.getUser().getId().toString(), Map.of("username", token.getUser().getUsername())));
                });
    }

    private IssuedRefreshToken issueRefreshToken(UserPrincipal principal) {
        String rawToken = UUID.randomUUID() + "." + UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByIdAndActiveTrue(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found")));
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setExpiresAt(Instant.now().plus(properties.getSecurity().getRefreshTokenDays(), ChronoUnit.DAYS));
        return new IssuedRefreshToken(rawToken, refreshTokenRepository.save(refreshToken));
    }

    private org.springframework.security.core.Authentication authenticate(LoginRequest request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (AuthenticationException ex) {
            recordFailedLogin(request.username(), ex.getMessage());
            throw ex;
        }
    }

    private void recordFailedLogin(String username, String reason) {
        userRepository.findByUsernameIgnoreCase(username).ifPresent(user -> {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setAccountLockedUntil(Instant.now().plusSeconds(15 * 60));
            }
            auditService.record(AuditEventType.AUTHENTICATION, "LOGIN_FAILED", "AppUser",
                    user.getId().toString(), reason);
        });
    }

    private void resetFailedLoginAttempts(String username) {
        userRepository.findByUsernameIgnoreCase(username).ifPresent(user -> {
            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
        });
    }

    private AuthResponse response(UserPrincipal principal, IssuedRefreshToken refreshToken) {
        return new AuthResponse(
                jwtService.generateAccessToken(principal),
                refreshToken.rawToken(),
                refreshToken.entity().getExpiresAt(),
                "Bearer",
                userMapper.toResponse(refreshToken.entity().getUser()));
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", ex);
        }
    }

    private record IssuedRefreshToken(String rawToken, RefreshToken entity) {
    }
}
