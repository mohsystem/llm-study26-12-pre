package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-ttl-seconds:900}") long accessTtlSeconds,
            @Value("${app.jwt.refresh-ttl-seconds:1209600}") long refreshTtlSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    public String issueAccessToken(String userPublicRef, String email, Role role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userPublicRef)
                .claims(Map.of(
                        "email", email,
                        "role", role.name(),
                        "typ", "access"
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public String issueRefreshToken(String userPublicRef) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userPublicRef)
                .claims(Map.of("typ", "refresh"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
                .signWith(key)
                .compact();
    }

    public Claims parse(String jwt) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isRefreshToken(Claims claims) {
        return "refresh".equals(claims.get("typ", String.class));
    }

    public String getSubject(Claims claims) {
        return claims.getSubject();
    }
}
