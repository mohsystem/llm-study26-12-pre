package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class DirectoryValidationRequest {

    @Email
    @Size(max = 180)
    private String email;

    @Size(max = 80)
    private String firstName;

    @Size(max = 80)
    private String lastName;

    @Size(max = 80)
    private String employeeId;

    public DirectoryValidationRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
}
