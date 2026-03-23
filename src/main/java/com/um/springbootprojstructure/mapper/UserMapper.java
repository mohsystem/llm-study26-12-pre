package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserProfileResponse;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserCreateRequest req) {
        User u = new User();
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        u.setRole(Role.USER); // default
        u.setActive(true);
        return u;
    }

    public static UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getPublicRef(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getRole(),
                u.isActive()
        );
    }

    public static UserProfileResponse toProfile(User u) {
        return new UserProfileResponse(
                u.getPublicRef(),
                u.getFirstName(),
                u.getLastName(),
                u.isActive()
        );
    }
}
