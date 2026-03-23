package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.AuthTokenResponse;
import com.um.springbootprojstructure.dto.LogoutResponse;

public interface AuthService {
    AuthTokenResponse login(String email, String password);
    AuthTokenResponse refresh(String refreshToken);
    LogoutResponse logout(String refreshToken);
}
