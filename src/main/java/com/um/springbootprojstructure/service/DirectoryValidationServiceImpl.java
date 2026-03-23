package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.DirectoryMatchResponse;
import com.um.springbootprojstructure.dto.DirectoryValidationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attributes;
import java.util.List;
import java.util.Map;

@Service
public class DirectoryValidationServiceImpl implements DirectoryValidationService {

    private final LdapTemplate ldapTemplate;
    private final PromptLogService promptLogService;

    private final boolean mock;
    private final String userSearchBase;
    private final String userObjectClass;

    public DirectoryValidationServiceImpl(LdapTemplate ldapTemplate,
                                         PromptLogService promptLogService,
                                         @Value("${app.ldap.mock:true}") boolean mock,
                                         @Value("${app.ldap.user-search-base:ou=people}") String userSearchBase,
                                         @Value("${app.ldap.user-object-class:person}") String userObjectClass) {
        this.ldapTemplate = ldapTemplate;
        this.promptLogService = promptLogService;
        this.mock = mock;
        this.userSearchBase = userSearchBase;
        this.userObjectClass = userObjectClass;
    }

    @Override
    public DirectoryMatchResponse validate(DirectoryValidationRequest request) {
        promptLogService.logPrompt("Task: validateDirectory email=" + request.getEmail()
                + " employeeId=" + request.getEmployeeId());

        if (isBlank(request.getEmail()) && isBlank(request.getEmployeeId())
                && (isBlank(request.getFirstName()) || isBlank(request.getLastName()))) {
            throw new IllegalArgumentException("Provide email, employeeId, or both firstName and lastName");
        }

        if (mock) {
            return mockResponse(request);
        }

        try {
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("objectClass", userObjectClass));

            if (!isBlank(request.getEmployeeId())) {
                filter.and(new EqualsFilter("employeeNumber", request.getEmployeeId().trim()));
            } else if (!isBlank(request.getEmail())) {
                filter.and(new EqualsFilter("mail", request.getEmail().trim()));
            } else {
                filter.and(new EqualsFilter("givenName", request.getFirstName().trim()));
                filter.and(new EqualsFilter("sn", request.getLastName().trim()));
            }

            List<DirectoryMatchResponse> matches = ldapTemplate.search(
                    userSearchBase,
                    filter.encode(),
                    (AttributesMapper<DirectoryMatchResponse>) attrs -> mapMatch(attrs)
            );

            if (matches.isEmpty()) {
                DirectoryMatchResponse res = new DirectoryMatchResponse();
                res.setStatus(DirectoryMatchResponse.Status.NOT_FOUND);
                res.setMessage("No directory entry matched");
                return res;
            }
            if (matches.size() > 1) {
                DirectoryMatchResponse res = new DirectoryMatchResponse();
                res.setStatus(DirectoryMatchResponse.Status.AMBIGUOUS);
                res.setMessage("Multiple directory entries matched: " + matches.size());
                return res;
            }

            DirectoryMatchResponse res = matches.get(0);
            res.setStatus(DirectoryMatchResponse.Status.MATCHED);
            res.setMessage("Matched directory entry");
            return res;

        } catch (Exception ex) {
            DirectoryMatchResponse res = new DirectoryMatchResponse();
            res.setStatus(DirectoryMatchResponse.Status.ERROR);
            res.setMessage("Directory lookup error: " + ex.getMessage());
            return res;
        }
    }

    private DirectoryMatchResponse mapMatch(Attributes attrs) throws NamingException, javax.naming.NamingException {
        DirectoryMatchResponse res = new DirectoryMatchResponse();

        res.setUid(getAttr(attrs, "uid"));
        res.setEmail(getAttr(attrs, "mail"));
        res.setFirstName(getAttr(attrs, "givenName"));
        res.setLastName(getAttr(attrs, "sn"));
        res.setEmployeeId(getAttr(attrs, "employeeNumber"));

        res.setAttributes(Map.of(
                "uid", res.getUid(),
                "mail", res.getEmail(),
                "givenName", res.getFirstName(),
                "sn", res.getLastName(),
                "employeeNumber", res.getEmployeeId()
        ));

        res.setDn(null);
        return res;
    }

    private String getAttr(Attributes attrs, String name) throws javax.naming.NamingException {
        if (attrs == null || attrs.get(name) == null) return null;
        Object val = attrs.get(name).get();
        return val == null ? null : String.valueOf(val);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private DirectoryMatchResponse mockResponse(DirectoryValidationRequest request) {
        DirectoryMatchResponse res = new DirectoryMatchResponse();

        if (!isBlank(request.getEmail()) && request.getEmail().toLowerCase().endsWith("@example.com")) {
            res.setStatus(DirectoryMatchResponse.Status.MATCHED);
            res.setMessage("Mock match");
            res.setUid("mockuid");
            res.setEmail(request.getEmail());
            res.setFirstName(request.getFirstName() == null ? "Mock" : request.getFirstName());
            res.setLastName(request.getLastName() == null ? "User" : request.getLastName());
            res.setEmployeeId(request.getEmployeeId() == null ? "E0001" : request.getEmployeeId());
            res.setAttributes(Map.of(
                    "uid", res.getUid(),
                    "mail", res.getEmail(),
                    "givenName", res.getFirstName(),
                    "sn", res.getLastName(),
                    "employeeNumber", res.getEmployeeId()
            ));
            return res;
        }

        res.setStatus(DirectoryMatchResponse.Status.NOT_FOUND);
        res.setMessage("Mock not found");
        return res;
    }
}
