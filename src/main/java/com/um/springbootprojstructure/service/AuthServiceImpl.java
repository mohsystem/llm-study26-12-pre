package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.AuthTokenResponse;
import com.um.springbootprojstructure.dto.LogoutResponse;
import com.um.springbootprojstructure.entity.RefreshToken;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.RefreshTokenRepository;
import com.um.springbootprojstructure.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PromptLogService promptLogService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           JwtService jwtService,
                           PromptLogService promptLogService,
                           RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.promptLogService = promptLogService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AuthTokenResponse login(String email, String password) {
        promptLogService.logPrompt("Task: login called with email=" + email);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        String access = jwtService.issueAccessToken(user.getPublicRef(), user.getEmail(), user.getRole());
        String refresh = jwtService.issueRefreshToken(user.getPublicRef());
        refreshTokenRepository.save(new RefreshToken(refresh, user.getId()));
        return new AuthTokenResponse(access, refresh);
    }

    @Override
    public AuthTokenResponse refresh(String refreshToken) {
        promptLogService.logPrompt("Task: refresh called");

        Claims claims = jwtService.parse(refreshToken);
        if (!jwtService.isRefreshToken(claims)) {
            throw new IllegalArgumentException("Provided token is not a refresh token");
        }

        RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is not recognized"));

        if (stored.isRevoked()) {
            throw new IllegalArgumentException("Refresh token is revoked");
        }

        String publicRef = jwtService.getSubject(claims);

        User user = userRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException("User not found for publicRef: " + publicRef));

        if (!user.isActive()) {
            throw new IllegalArgumentException("User is deactivated");
        }

        stored.setRevoked(true);
        stored.setRevokedAt(OffsetDateTime.now());
        refreshTokenRepository.save(stored);

        String newAccess = jwtService.issueAccessToken(user.getPublicRef(), user.getEmail(), user.getRole());
        String newRefresh = jwtService.issueRefreshToken(user.getPublicRef());
        refreshTokenRepository.save(new RefreshToken(newRefresh, user.getId()));
        return new AuthTokenResponse(newAccess, newRefresh);
    }

    @Override
    public LogoutResponse logout(String refreshToken) {
        promptLogService.logPrompt("Task: logout called");

        RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is not recognized"));

        if (stored.isRevoked()) {
            return new LogoutResponse(true, "Already logged out");
        }

        stored.setRevoked(true);
        stored.setRevokedAt(OffsetDateTime.now());
        refreshTokenRepository.save(stored);

        return new LogoutResponse(true, "Logged out successfully");
    }
}
