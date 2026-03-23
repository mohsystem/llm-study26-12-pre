package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.service.PasswordRulesService;
import com.um.springbootprojstructure.service.PromptLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/accounts")
public class AdminAccountController {

    private final PasswordRulesService passwordRulesService;
    private final PromptLogService promptLogService;

    public AdminAccountController(PasswordRulesService passwordRulesService, PromptLogService promptLogService) {
        this.passwordRulesService = passwordRulesService;
        this.promptLogService = promptLogService;
    }

    @GetMapping("/password-rules")
    public PasswordRulesResponse getRules() {
        promptLogService.logPrompt("HTTP GET /api/admin/accounts/password-rules");
        return passwordRulesService.getActiveRules();
    }

    @PutMapping("/password-rules")
    public PasswordRulesResponse updateRules(@Valid @RequestBody PasswordRulesRequest request) {
        promptLogService.logPrompt("HTTP PUT /api/admin/accounts/password-rules");
        return passwordRulesService.updateRules(request);
    }
}
