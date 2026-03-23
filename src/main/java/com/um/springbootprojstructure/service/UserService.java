package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserProfileResponse;
import com.um.springbootprojstructure.dto.UserProfileUpdateRequest;
import com.um.springbootprojstructure.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long id);

    UserProfileResponse getUserProfileByPublicRef(String publicRef);
    UserProfileResponse updateUserProfile(String publicRef, UserProfileUpdateRequest request);

    List<UserResponse> listUsers();

    void deactivateUser(Long id);

    void promoteToAdmin(Long id);
}
