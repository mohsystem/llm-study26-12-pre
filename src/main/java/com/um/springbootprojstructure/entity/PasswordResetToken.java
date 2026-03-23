package com.um.springbootprojstructure.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "password_reset_tokens", indexes = {
        @Index(name = "idx_prt_token", columnList = "token", unique = true),
        @Index(name = "idx_prt_user_id", columnList = "userId")
})
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Column(nullable = true)
    private OffsetDateTime usedAt;

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    public PasswordResetToken() {}

    public PasswordResetToken(String token, Long userId, OffsetDateTime expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public Long getId() { return id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }

    public OffsetDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(OffsetDateTime usedAt) { this.usedAt = usedAt; }
}
