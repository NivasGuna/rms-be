package com.arigs.rms.security;

import com.arigs.rms.entity.AppUser;
import com.arigs.rms.entity.Role;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security principal wrapping an RMS user.
 */
public class UserPrincipal implements UserDetails {

    private final UUID id;
    private final String username;
    private final String password;
    private final boolean active;
    private final Instant accountLockedUntil;
    private final Instant passwordExpiresAt;
    private final Set<Role> roles;

    public UserPrincipal(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPasswordHash();
        this.active = user.isActive();
        this.accountLockedUntil = user.getAccountLockedUntil();
        this.passwordExpiresAt = user.getPasswordExpiresAt();
        this.roles = Set.copyOf(user.getRoles());
    }

    public UUID getId() {
        return id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active && (accountLockedUntil == null || accountLockedUntil.isBefore(Instant.now()));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active && (passwordExpiresAt == null || passwordExpiresAt.isAfter(Instant.now()));
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
