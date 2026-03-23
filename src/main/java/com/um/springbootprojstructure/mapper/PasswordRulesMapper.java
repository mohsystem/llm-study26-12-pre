package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.entity.PasswordRules;

public class PasswordRulesMapper {

    private PasswordRulesMapper() {}

    public static PasswordRulesResponse toResponse(PasswordRules e) {
        return new PasswordRulesResponse(
                e.getMinLength(),
                e.getMaxLength(),
                e.isRequireUppercase(),
                e.isRequireLowercase(),
                e.isRequireDigit(),
                e.isRequireSpecial(),
                e.getMinSpecial(),
                e.isEnabled()
        );
    }

    public static void apply(PasswordRules target, PasswordRulesRequest req) {
        target.setMinLength(req.getMinLength());
        target.setMaxLength(req.getMaxLength());
        target.setRequireUppercase(req.isRequireUppercase());
        target.setRequireLowercase(req.isRequireLowercase());
        target.setRequireDigit(req.isRequireDigit());
        target.setRequireSpecial(req.isRequireSpecial());
        target.setMinSpecial(req.getMinSpecial());
        target.setEnabled(req.isEnabled());
    }
}
