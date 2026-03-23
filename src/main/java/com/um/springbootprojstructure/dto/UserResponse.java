package com.um.springbootprojstructure.dto;

import com.um.springbootprojstructure.entity.Role;

public class UserResponse {
    private Long id;
    private String publicRef;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean active;

    public UserResponse() {}

    public UserResponse(Long id, String publicRef, String firstName, String lastName, String email, Role role, boolean active) {
        this.id = id;
        this.publicRef = publicRef;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPublicRef() { return publicRef; }
    public void setPublicRef(String publicRef) { this.publicRef = publicRef; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
