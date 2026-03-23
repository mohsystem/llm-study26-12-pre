package com.um.springbootprojstructure.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class DirectoryMatchResponse {

    public enum Status {
        MATCHED,
        NOT_FOUND,
        AMBIGUOUS,
        ERROR
    }

    private Status status;
    private String message;

    private String dn;
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String employeeId;

    private Map<String, Object> attributes = new LinkedHashMap<>();

    public DirectoryMatchResponse() {}

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDn() { return dn; }
    public void setDn(String dn) { this.dn = dn; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
}
