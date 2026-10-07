package com.arigs.rms.security;

import com.arigs.rms.config.RmsProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Creates and validates JWT access tokens.
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final Duration accessTokenTtl;

    public JwtService(RmsProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecurity().getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtl = Duration.ofMinutes(properties.getSecurity().getAccessTokenMinutes());
    }

    public String generateAccessToken(UserPrincipal principal) {
        Instant now = Instant.now();
        List<String> authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(principal.getUsername())
                .claim("uid", principal.getId().toString())
                .claim("roles", authorities)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenTtl)))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return claims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && claims(token).getExpiration().after(new Date());
    }

    private Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
