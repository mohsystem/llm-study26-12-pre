package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.*;

public interface AccountAuthService {
    RegisterResponse register(RegisterRequest request);
    GenericActionResponse changePassword(String email, ChangePasswordRequest request);
    GenericActionResponse confirmPasswordReset(PasswordResetConfirmRequest request);
}
