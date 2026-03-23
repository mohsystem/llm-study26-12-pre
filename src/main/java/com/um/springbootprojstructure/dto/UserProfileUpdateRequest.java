package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequest {

    // Allowed fields (no email/role here by spec)
    @Size(max = 80)
    private String firstName;

    @Size(max = 80)
    private String lastName;

    public UserProfileUpdateRequest() {}

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
