package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.AuthLoginRequest;
import com.um.springbootprojstructure.dto.AuthTokenResponse;
import com.um.springbootprojstructure.dto.LogoutRequest;
import com.um.springbootprojstructure.dto.LogoutResponse;
import com.um.springbootprojstructure.dto.RefreshTokenRequest;
import com.um.springbootprojstructure.service.AuthService;
import com.um.springbootprojstructure.service.PromptLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PromptLogService promptLogService;

    public AuthController(AuthService authService, PromptLogService promptLogService) {
        this.authService = authService;
        this.promptLogService = promptLogService;
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody AuthLoginRequest request) {
        promptLogService.logPrompt("HTTP POST /api/auth/login email=" + request.getEmail());
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        promptLogService.logPrompt("HTTP POST /api/auth/refresh");
        return authService.refresh(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public LogoutResponse logout(@Valid @RequestBody LogoutRequest request) {
        promptLogService.logPrompt("HTTP POST /api/auth/logout");
        return authService.logout(request.getRefreshToken());
    }
}
