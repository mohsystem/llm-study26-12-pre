package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminUserMergeRequest {

    @NotBlank
    private String sourcePublicRef;

    @NotBlank
    private String targetPublicRef;

    public AdminUserMergeRequest() {}

    public String getSourcePublicRef() { return sourcePublicRef; }
    public void setSourcePublicRef(String sourcePublicRef) { this.sourcePublicRef = sourcePublicRef; }

    public String getTargetPublicRef() { return targetPublicRef; }
    public void setTargetPublicRef(String targetPublicRef) { this.targetPublicRef = targetPublicRef; }
}
