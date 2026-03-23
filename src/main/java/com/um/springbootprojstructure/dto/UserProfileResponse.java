package com.um.springbootprojstructure.dto;

public class UserProfileResponse {

    private String publicRef;
    private String firstName;
    private String lastName;
    private boolean active;

    public UserProfileResponse() {}

    public UserProfileResponse(String publicRef, String firstName, String lastName, boolean active) {
        this.publicRef = publicRef;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public String getPublicRef() { return publicRef; }
    public void setPublicRef(String publicRef) { this.publicRef = publicRef; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
