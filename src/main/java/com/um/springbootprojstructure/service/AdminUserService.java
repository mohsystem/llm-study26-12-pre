package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.AdminUserMergeResultResponse;
import com.um.springbootprojstructure.dto.UpdateUserRoleResponse;
import com.um.springbootprojstructure.entity.Role;

public interface AdminUserService {
    AdminUserMergeResultResponse mergeUsers(String sourcePublicRef, String targetPublicRef);
    UpdateUserRoleResponse updateUserRole(Long id, Role role);
}
