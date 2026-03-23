package com.um.springbootprojstructure.dto;

public class PasswordRulesResponse {

    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecial;
    private int minSpecial;
    private boolean enabled;

    public PasswordRulesResponse() {}

    public PasswordRulesResponse(int minLength, int maxLength,
                                 boolean requireUppercase, boolean requireLowercase,
                                 boolean requireDigit, boolean requireSpecial,
                                 int minSpecial, boolean enabled) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.requireUppercase = requireUppercase;
        this.requireLowercase = requireLowercase;
        this.requireDigit = requireDigit;
        this.requireSpecial = requireSpecial;
        this.minSpecial = minSpecial;
        this.enabled = enabled;
    }

    public int getMinLength() { return minLength; }
    public void setMinLength(int minLength) { this.minLength = minLength; }

    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }

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

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
