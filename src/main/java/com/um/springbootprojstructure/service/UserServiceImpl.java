package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserProfileResponse;
import com.um.springbootprojstructure.dto.UserProfileUpdateRequest;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.mapper.UserMapper;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PromptLogService promptLogService;

    public UserServiceImpl(UserRepository userRepository, PromptLogService promptLogService) {
        this.userRepository = userRepository;
        this.promptLogService = promptLogService;
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        promptLogService.logPrompt("Task: createUser called with email=" + request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User user = UserMapper.toEntity(request);
        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        promptLogService.logPrompt("Task: getUserById called with id=" + id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfileByPublicRef(String publicRef) {
        promptLogService.logPrompt("Task: getUserProfileByPublicRef called with publicRef=" + publicRef);

        User user = userRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException("User not found for publicRef: " + publicRef));

        return UserMapper.toProfile(user);
    }

    @Override
    public UserProfileResponse updateUserProfile(String publicRef, UserProfileUpdateRequest request) {
        promptLogService.logPrompt("Task: updateUserProfile called with publicRef=" + publicRef);

        User user = userRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException("User not found for publicRef: " + publicRef));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        User saved = userRepository.save(user);
        return UserMapper.toProfile(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> listUsers() {
        promptLogService.logPrompt("Task: listUsers called");
        return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
    }

    @Override
    public void deactivateUser(Long id) {
        promptLogService.logPrompt("Task: deactivateUser called with id=" + id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void promoteToAdmin(Long id) {
        promptLogService.logPrompt("Task: promoteToAdmin called with id=" + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
