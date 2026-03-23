package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.PasswordRulesRequest;
import com.um.springbootprojstructure.dto.PasswordRulesResponse;
import com.um.springbootprojstructure.entity.PasswordRules;
import com.um.springbootprojstructure.mapper.PasswordRulesMapper;
import com.um.springbootprojstructure.repository.PasswordRulesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PasswordRulesServiceImpl implements PasswordRulesService {

    private final PasswordRulesRepository repo;
    private final PromptLogService promptLogService;

    public PasswordRulesServiceImpl(PasswordRulesRepository repo, PromptLogService promptLogService) {
        this.repo = repo;
        this.promptLogService = promptLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordRulesResponse getActiveRules() {
        promptLogService.logPrompt("Task: getActivePasswordRules called");
        PasswordRules rules = getOrCreateDefaults();
        return PasswordRulesMapper.toResponse(rules);
    }

    @Override
    public PasswordRulesResponse updateRules(PasswordRulesRequest request) {
        promptLogService.logPrompt("Task: updatePasswordRules called");

        if (request.getMinLength() > request.getMaxLength()) {
            throw new IllegalArgumentException("minLength must be <= maxLength");
        }
        if (!request.isRequireSpecial() && request.getMinSpecial() > 0) {
            request.setMinSpecial(0);
        }
        if (request.isRequireSpecial() && request.getMinSpecial() <= 0) {
            throw new IllegalArgumentException("minSpecial must be > 0 when requireSpecial is true");
        }

        PasswordRules rules = getLatestOrNull();
        if (rules == null) rules = new PasswordRules();

        PasswordRulesMapper.apply(rules, request);
        PasswordRules saved = repo.save(rules);

        return PasswordRulesMapper.toResponse(saved);
    }

    private PasswordRules getOrCreateDefaults() {
        PasswordRules latest = getLatestOrNull();
        if (latest != null) return latest;
        return repo.save(new PasswordRules());
    }

    private PasswordRules getLatestOrNull() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
