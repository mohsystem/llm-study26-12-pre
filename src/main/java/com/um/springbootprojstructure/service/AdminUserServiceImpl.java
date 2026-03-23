package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.AdminUserMergeResultResponse;
import com.um.springbootprojstructure.dto.UpdateUserRoleResponse;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final PromptLogService promptLogService;

    public AdminUserServiceImpl(UserRepository userRepository, PromptLogService promptLogService) {
        this.userRepository = userRepository;
        this.promptLogService = promptLogService;
    }

    @Override
    public AdminUserMergeResultResponse mergeUsers(String sourcePublicRef, String targetPublicRef) {
        promptLogService.logPrompt("Task: mergeUsers source=" + sourcePublicRef + " target=" + targetPublicRef);

        if (sourcePublicRef.equals(targetPublicRef)) {
            throw new IllegalArgumentException("sourcePublicRef and targetPublicRef must be different");
        }

        User source = userRepository.findByPublicRef(sourcePublicRef)
                .orElseThrow(() -> new IllegalArgumentException("Source user not found: " + sourcePublicRef));
        User target = userRepository.findByPublicRef(targetPublicRef)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found: " + targetPublicRef));

        AdminUserMergeResultResponse result = new AdminUserMergeResultResponse();
        result.setSourcePublicRef(sourcePublicRef);
        result.setTargetPublicRef(targetPublicRef);
        result.setActions(new ArrayList<>());

        if (isBlank(target.getFirstName()) && !isBlank(source.getFirstName())) {
            target.setFirstName(source.getFirstName());
            result.getActions().add("Copied firstName from source -> target");
        }

        if (isBlank(target.getLastName()) && !isBlank(source.getLastName())) {
            target.setLastName(source.getLastName());
            result.getActions().add("Copied lastName from source -> target");
        }

        if (isBlank(target.getEmail()) && !isBlank(source.getEmail())) {
            target.setEmail(source.getEmail());
            result.getActions().add("Copied email from source -> target");
        } else if (!equalsIgnoreCase(target.getEmail(), source.getEmail())) {
            result.getActions().add("Kept target email; source email differs (" + source.getEmail() + ")");
        }

        if (!target.isActive() && source.isActive()) {
            target.setActive(true);
            result.getActions().add("Activated target because source was active");
        }

        if (target.getRole() != Role.ADMIN && source.getRole() == Role.ADMIN) {
            target.setRole(Role.ADMIN);
            result.getActions().add("Upgraded target role to ADMIN based on source role");
        }

        if (isBlank(target.getPasswordHash()) && !isBlank(source.getPasswordHash())) {
            target.setPasswordHash(source.getPasswordHash());
            result.getActions().add("Copied passwordHash from source -> target");
        } else {
            result.getActions().add("Kept target passwordHash");
        }

        userRepository.save(target);

        if (source.isActive()) {
            source.setActive(false);
            userRepository.save(source);
            result.getActions().add("Deactivated source account after merge");
        } else {
            result.getActions().add("Source account already inactive");
        }

        result.setFinalFirstName(target.getFirstName());
        result.setFinalLastName(target.getLastName());
        result.setFinalEmail(target.getEmail());
        result.setFinalActive(target.isActive());

        return result;
    }

    @Override
    public UpdateUserRoleResponse updateUserRole(Long id, Role role) {
        promptLogService.logPrompt("Task: updateUserRole called with id=" + id + " role=" + role);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        user.setRole(role);
        User saved = userRepository.save(user);

        return new UpdateUserRoleResponse(saved.getId(), saved.getPublicRef(), saved.getRole());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean equalsIgnoreCase(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }
}
