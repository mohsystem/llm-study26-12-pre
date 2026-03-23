package com.um.springbootprojstructure.dto;

import com.um.springbootprojstructure.entity.Role;

public class UpdateUserRoleResponse {
    private Long id;
    private String publicRef;
    private Role role;

    public UpdateUserRoleResponse() {}

    public UpdateUserRoleResponse(Long id, String publicRef, Role role) {
        this.id = id;
        this.publicRef = publicRef;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPublicRef() { return publicRef; }
    public void setPublicRef(String publicRef) { this.publicRef = publicRef; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
