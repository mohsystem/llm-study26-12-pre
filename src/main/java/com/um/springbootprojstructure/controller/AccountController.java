package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.*;
import com.um.springbootprojstructure.service.AccountAuthService;
import com.um.springbootprojstructure.service.PasswordPolicyValidator;
import com.um.springbootprojstructure.service.PromptLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountAuthService accountAuthService;
    private final PasswordPolicyValidator passwordPolicyValidator;
    private final PromptLogService promptLogService;

    public AccountController(AccountAuthService accountAuthService,
                             PasswordPolicyValidator passwordPolicyValidator,
                             PromptLogService promptLogService) {
        this.accountAuthService = accountAuthService;
        this.passwordPolicyValidator = passwordPolicyValidator;
        this.promptLogService = promptLogService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        promptLogService.logPrompt("HTTP POST /api/accounts/register email=" + request.getEmail());
        return accountAuthService.register(request);
    }

    @PostMapping("/password/validate")
    public PasswordValidationResultResponse validatePassword(@RequestBody(required = false) String password) {
        promptLogService.logPrompt("HTTP POST /api/accounts/password/validate");
        return passwordPolicyValidator.validate(password);
    }

    @PostMapping("/password/change")
    public GenericActionResponse changePassword(@RequestParam("email") String email,
                                                @Valid @RequestBody ChangePasswordRequest request) {
        promptLogService.logPrompt("HTTP POST /api/accounts/password/change email=" + email);
        return accountAuthService.changePassword(email, request);
    }

    @PostMapping("/password/reset/confirm")
    public GenericActionResponse confirmReset(@Valid @RequestBody PasswordResetConfirmRequest request) {
        promptLogService.logPrompt("HTTP POST /api/accounts/password/reset/confirm");
        return accountAuthService.confirmPasswordReset(request);
    }
}
