package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.*;
import com.um.springbootprojstructure.entity.PasswordResetToken;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.PasswordResetTokenRepository;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.HexFormat;

@Service
@Transactional
public class AccountAuthServiceImpl implements AccountAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyValidator passwordPolicyValidator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PromptLogService promptLogService;

    public AccountAuthServiceImpl(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  PasswordPolicyValidator passwordPolicyValidator,
                                  PasswordResetTokenRepository passwordResetTokenRepository,
                                  PromptLogService promptLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordPolicyValidator = passwordPolicyValidator;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.promptLogService = promptLogService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        promptLogService.logPrompt("Task: register called email=" + request.getEmail());

        PasswordValidationResultResponse pv = passwordPolicyValidator.validate(request.getPassword());
        if (pv.getStatus() == PasswordValidationResultResponse.Status.REJECTED) {
            throw new IllegalArgumentException("PASSWORD_RULES_REJECTED: " + String.join(",", pv.getCodes()));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User u = new User();
        u.setFirstName(request.getFirstName());
        u.setLastName(request.getLastName());
        u.setEmail(request.getEmail());
        u.setRole(Role.USER);
        u.setActive(true);
        u.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(u);
        return new RegisterResponse(saved.getPublicRef(), saved.getEmail());
    }

    @Override
    public GenericActionResponse changePassword(String email, ChangePasswordRequest request) {
        promptLogService.logPrompt("Task: changePassword called email=" + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        PasswordValidationResultResponse pv = passwordPolicyValidator.validate(request.getNewPassword());
        if (pv.getStatus() == PasswordValidationResultResponse.Status.REJECTED) {
            throw new IllegalArgumentException("PASSWORD_RULES_REJECTED: " + String.join(",", pv.getCodes()));
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new GenericActionResponse(true, "Password changed");
    }

    @Override
    public GenericActionResponse confirmPasswordReset(PasswordResetConfirmRequest request) {
        promptLogService.logPrompt("Task: confirmPasswordReset called");

        PasswordValidationResultResponse pv = passwordPolicyValidator.validate(request.getNewPassword());
        if (pv.getStatus() == PasswordValidationResultResponse.Status.REJECTED) {
            throw new IllegalArgumentException("PASSWORD_RULES_REJECTED: " + String.join(",", pv.getCodes()));
        }

        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (token.isUsed()) {
            throw new IllegalArgumentException("Reset token already used");
        }
        if (token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Reset token expired");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for token"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        token.setUsed(true);
        token.setUsedAt(OffsetDateTime.now());
        passwordResetTokenRepository.save(token);

        return new GenericActionResponse(true, "Password reset confirmed");
    }

    public PasswordResetToken createResetTokenForUser(User user, long ttlMinutes) {
        byte[] buf = new byte[32];
        new SecureRandom().nextBytes(buf);
        String tokenValue = HexFormat.of().formatHex(buf);

        PasswordResetToken token = new PasswordResetToken(
                tokenValue,
                user.getId(),
                OffsetDateTime.now().plusMinutes(ttlMinutes)
        );
        return passwordResetTokenRepository.save(token);
    }
}
