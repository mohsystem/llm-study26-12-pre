package com.um.springbootprojstructure.dto;

public class RegisterResponse {
    private String publicRef;
    private String email;

    public RegisterResponse() {}

    public RegisterResponse(String publicRef, String email) {
        this.publicRef = publicRef;
        this.email = email;
    }

    public String getPublicRef() { return publicRef; }
    public void setPublicRef(String publicRef) { this.publicRef = publicRef; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
