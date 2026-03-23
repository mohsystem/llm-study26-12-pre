package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PasswordRulesRequest {

    @Min(4) @Max(256)
    private int minLength = 8;

    @Min(4) @Max(256)
    private int maxLength = 128;

    private boolean requireUppercase = true;
    private boolean requireLowercase = true;
    private boolean requireDigit = true;

    private boolean requireSpecial = false;

    @Min(0) @Max(10)
    private int minSpecial = 1;

    private boolean enabled = true;

    public PasswordRulesRequest() {}

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
