package com.um.springbootprojstructure.config;

import com.um.springbootprojstructure.dto.PasswordValidationResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {

        if (ex.getMessage() != null && ex.getMessage().startsWith("PASSWORD_RULES_REJECTED:")) {
            String codesPart = ex.getMessage().substring("PASSWORD_RULES_REJECTED:".length()).trim();

            List<String> codes = codesPart.isBlank()
                    ? List.of()
                    : Arrays.stream(codesPart.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();

            List<String> messages = codes.stream().map(GlobalExceptionHandler::messageForCode).toList();

            PasswordValidationResultResponse body =
                    PasswordValidationResultResponse.rejected(new ArrayList<>(codes), new ArrayList<>(messages));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LinkedHashMap<>(Map.of(
                    "timestamp", OffsetDateTime.now().toString(),
                    "error", "PasswordRejected",
                    "validation", body
            )));
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private static String messageForCode(String code) {
        return switch (code) {
            case "MIN_LENGTH" -> "Password is too short";
            case "MAX_LENGTH" -> "Password is too long";
            case "REQUIRE_UPPERCASE" -> "Password must contain an uppercase character";
            case "REQUIRE_LOWERCASE" -> "Password must contain a lowercase character";
            case "REQUIRE_DIGIT" -> "Password must contain a digit";
            case "REQUIRE_SPECIAL" -> "Password must contain a special character";
            default -> "Password does not meet policy requirements";
        };
    }
}
