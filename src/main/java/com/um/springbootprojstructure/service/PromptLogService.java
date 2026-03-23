package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.UserPromptLoggerConfig;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class PromptLogService {

    public void logPrompt(String promptText) {
        String entry = "[PROMPT] " + OffsetDateTime.now() + " - " + promptText;
        UserPromptLoggerConfig.appendPrompt(entry);
    }
}
