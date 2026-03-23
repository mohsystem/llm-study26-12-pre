package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.UserCreateRequest;
import com.um.springbootprojstructure.dto.UserProfileResponse;
import com.um.springbootprojstructure.dto.UserProfileUpdateRequest;
import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.service.PromptLogService;
import com.um.springbootprojstructure.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PromptLogService promptLogService;

    public UserController(UserService userService, PromptLogService promptLogService) {
        this.userService = userService;
        this.promptLogService = promptLogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        promptLogService.logPrompt("HTTP POST /api/users payload email=" + request.getEmail());
        return userService.createUser(request);
    }

    /**
     * REQUIRED by spec:
     * GET /api/users/{publicRef} returns user profile JSON for public account reference.
     */
    @GetMapping("/{publicRef}")
    public UserProfileResponse getProfile(@PathVariable String publicRef) {
        promptLogService.logPrompt("HTTP GET /api/users/" + publicRef);
        return userService.getUserProfileByPublicRef(publicRef);
    }

    @PutMapping("/{publicRef}")
    public UserProfileResponse updateProfile(@PathVariable String publicRef,
                                             @Valid @RequestBody UserProfileUpdateRequest request) {
        promptLogService.logPrompt("HTTP PUT /api/users/" + publicRef);
        return userService.updateUserProfile(publicRef, request);
    }

    @GetMapping("/id/{id}")
    public UserResponse getById(@PathVariable Long id) {
        promptLogService.logPrompt("HTTP GET /api/users/id/" + id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> list() {
        promptLogService.logPrompt("HTTP GET /api/users");
        return userService.listUsers();
    }

    @PatchMapping("/id/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        promptLogService.logPrompt("HTTP PATCH /api/users/id/" + id + "/deactivate");
        userService.deactivateUser(id);
    }
}
