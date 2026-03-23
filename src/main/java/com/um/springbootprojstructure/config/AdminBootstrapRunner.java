package com.um.springbootprojstructure.config;

import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import com.um.springbootprojstructure.service.PromptLogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final AdminBootstrapProperties props;
    private final UserRepository userRepository;
    private final PromptLogService promptLogService;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrapRunner(AdminBootstrapProperties props,
                                UserRepository userRepository,
                                PromptLogService promptLogService,
                                PasswordEncoder passwordEncoder) {
        this.props = props;
        this.userRepository = userRepository;
        this.promptLogService = promptLogService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!props.isEnabled()) {
            promptLogService.logPrompt("Admin bootstrap disabled (app.bootstrap.admin.enabled=false)");
            return;
        }

        String email = props.getEmail();
        if (userRepository.existsByEmail(email)) {
            promptLogService.logPrompt("Admin bootstrap skipped; admin already exists: " + email);
            return;
        }

        User admin = new User();
        admin.setFirstName(props.getFirstName());
        admin.setLastName(props.getLastName());
        admin.setEmail(email);
        admin.setPasswordHash(passwordEncoder.encode(props.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);
        promptLogService.logPrompt("Admin bootstrap created admin: " + email + " (password set from properties)");
    }
}
