package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.dto.PasswordValidationResultResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordPolicyValidator {

    private final PasswordRulesService passwordRulesService;

    public PasswordPolicyValidator(PasswordRulesService passwordRulesService) {
        this.passwordRulesService = passwordRulesService;
    }

    public PasswordValidationResultResponse validate(String password) {
        PasswordRulesResponse rules = passwordRulesService.getActiveRules();

        List<String> codes = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        if (password == null) password = "";

        if (!rules.isEnabled()) {
            return PasswordValidationResultResponse.accepted();
        }

        int len = password.length();
        if (len < rules.getMinLength()) {
            codes.add("MIN_LENGTH");
            messages.add("Password length must be at least " + rules.getMinLength());
        }
        if (len > rules.getMaxLength()) {
            codes.add("MAX_LENGTH");
            messages.add("Password length must be at most " + rules.getMaxLength());
        }

        int upper = 0, lower = 0, digit = 0, special = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) upper++;
            else if (Character.isLowerCase(c)) lower++;
            else if (Character.isDigit(c)) digit++;
            else special++;
        }

        if (rules.isRequireUppercase() && upper < 1) {
            codes.add("REQUIRE_UPPERCASE");
            messages.add("Password must contain at least 1 uppercase character");
        }
        if (rules.isRequireLowercase() && lower < 1) {
            codes.add("REQUIRE_LOWERCASE");
            messages.add("Password must contain at least 1 lowercase character");
        }
        if (rules.isRequireDigit() && digit < 1) {
            codes.add("REQUIRE_DIGIT");
            messages.add("Password must contain at least 1 digit");
        }

        if (rules.isRequireSpecial()) {
            int minSpecial = Math.max(1, rules.getMinSpecial());
            if (special < minSpecial) {
                codes.add("REQUIRE_SPECIAL");
                messages.add("Password must contain at least " + minSpecial + " special character(s)");
            }
        }

        if (codes.isEmpty()) {
            return PasswordValidationResultResponse.accepted();
        }
        return PasswordValidationResultResponse.rejected(codes, messages);
    }
}
