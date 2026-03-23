package com.um.springbootprojstructure.config;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Configuration
public class UserPromptLoggerConfig {

    public static final Path PROMPT_LOG_PATH = Path.of("user-prompt.log");

    public UserPromptLoggerConfig() throws IOException {
        if (!Files.exists(PROMPT_LOG_PATH)) {
            Files.createFile(PROMPT_LOG_PATH);
        }
    }

    public static void appendPrompt(String text) {
        try {
            String line = text + System.lineSeparator();
            Files.writeString(
                    PROMPT_LOG_PATH,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Failed to write to user-prompt.log: " + e.getMessage());
        }
    }
}
