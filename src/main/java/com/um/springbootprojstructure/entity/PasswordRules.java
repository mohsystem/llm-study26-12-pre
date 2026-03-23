package com.um.springbootprojstructure.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "password_rules")
public class PasswordRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int minLength = 8;

    @Column(nullable = false)
    private boolean requireUppercase = true;

    @Column(nullable = false)
    private boolean requireLowercase = true;

    @Column(nullable = false)
    private boolean requireDigit = true;

    @Column(nullable = false)
    private boolean requireSpecial = false;

    @Column(nullable = false)
    private int minSpecial = 1;

    @Column(nullable = false)
    private int maxLength = 128;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public PasswordRules() {}

    public Long getId() { return id; }

    public int getMinLength() { return minLength; }
    public void setMinLength(int minLength) { this.minLength = minLength; }

    public boolean isRequireUppercase() { return requireUppercase; }
    public void setRequireUppercase(boolean requireUppercase) { this.requireUppercase = requireUppercase; }

    public boolean isRequireLowercase() { return requireLowercase; }
    public void setRequireLowercase(boolean requireLowercase) { this.requireLowercase = requireLowercase; }

    public boolean isRequireDigit() { return requireDigit; }
    public void setRequireDigit(boolean requireDigit) { this.requireDigit = requireDigit; }

    public boolean isRequireSpecial() { return requireSpecial; }
    public void setRequireSpecial(boolean requireSpecial) { this.requireSpecial = requireSpecial; }

    public int getMinSpecial() { return minSpecial; }
    public void setMinSpecial(int minSpecial) { this.minSpecial = minSpecial; }

    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
