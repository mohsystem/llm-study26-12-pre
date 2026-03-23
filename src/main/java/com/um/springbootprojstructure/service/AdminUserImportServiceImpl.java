package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.XmlImportRejectedRecord;
import com.um.springbootprojstructure.dto.XmlImportSummaryResponse;
import com.um.springbootprojstructure.dto.legacy.LegacyUserRecord;
import com.um.springbootprojstructure.dto.legacy.LegacyUsersExport;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.regex.Pattern;

@Service
@Transactional
public class AdminUserImportServiceImpl implements AdminUserImportService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PromptLogService promptLogService;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public AdminUserImportServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     PromptLogService promptLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.promptLogService = promptLogService;
    }

    @Override
    public XmlImportSummaryResponse importLegacyUsersXml(MultipartFile file) {
        promptLogService.logPrompt("Task: importLegacyUsersXml filename=" + file.getOriginalFilename()
                + " size=" + file.getSize());

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!name.endsWith(".xml")) {
            promptLogService.logPrompt("Import warning: uploaded file does not end with .xml");
        }

        LegacyUsersExport export = unmarshal(file);

        XmlImportSummaryResponse summary = new XmlImportSummaryResponse();
        summary.setTotalRecords(export.getUsers() == null ? 0 : export.getUsers().size());

        if (export.getUsers() == null) {
            return summary;
        }

        int index = 0;
        for (LegacyUserRecord rec : export.getUsers()) {
            index++;

            String email = safeTrim(rec.getEmail());
            String firstName = safeTrim(rec.getFirstName());
            String lastName = safeTrim(rec.getLastName());
            boolean active = rec.getActive() == null || rec.getActive();
            Role role = parseRole(rec.getRole());

            String validationError = validate(email, firstName, lastName);
            if (validationError != null) {
                summary.getRejected().add(new XmlImportRejectedRecord(index, email, validationError));
                continue;
            }

            if (userRepository.existsByEmail(email)) {
                summary.getSkippedEmails().add(email);
                continue;
            }

            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setActive(active);
            user.setRole(role);

            String tempPassword = "temp-" + java.util.UUID.randomUUID();
            user.setPasswordHash(passwordEncoder.encode(tempPassword));

            User saved = userRepository.save(user);
            summary.getImportedPublicRefs().add(saved.getPublicRef());
        }

        summary.setImportedCount(summary.getImportedPublicRefs().size());
        summary.setSkippedCount(summary.getSkippedEmails().size());
        summary.setRejectedCount(summary.getRejected().size());

        return summary;
    }

    private LegacyUsersExport unmarshal(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            JAXBContext ctx = JAXBContext.newInstance(LegacyUsersExport.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(is);
            return (LegacyUsersExport) obj;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse XML: " + e.getMessage(), e);
        }
    }

    private String validate(String email, String firstName, String lastName) {
        if (email == null || email.isBlank()) return "Missing email";
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Invalid email format";
        if (firstName == null || firstName.isBlank()) return "Missing firstName";
        if (lastName == null || lastName.isBlank()) return "Missing lastName";
        if (firstName.length() > 80) return "firstName too long (max 80)";
        if (lastName.length() > 80) return "lastName too long (max 80)";
        if (email.length() > 180) return "email too long (max 180)";
        return null;
    }

    private Role parseRole(String roleText) {
        if (roleText == null || roleText.isBlank()) return Role.USER;
        try {
            return Role.valueOf(roleText.trim().toUpperCase());
        } catch (Exception e) {
            return Role.USER;
        }
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
