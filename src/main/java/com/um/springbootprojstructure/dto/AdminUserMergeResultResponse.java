package com.um.springbootprojstructure.dto;

import java.util.ArrayList;
import java.util.List;

public class AdminUserMergeResultResponse {

    private String sourcePublicRef;
    private String targetPublicRef;

    private String finalFirstName;
    private String finalLastName;
    private String finalEmail;
    private boolean finalActive;

    private List<String> actions = new ArrayList<>();

    public AdminUserMergeResultResponse() {}

    public String getSourcePublicRef() { return sourcePublicRef; }
    public void setSourcePublicRef(String sourcePublicRef) { this.sourcePublicRef = sourcePublicRef; }

    public String getTargetPublicRef() { return targetPublicRef; }
    public void setTargetPublicRef(String targetPublicRef) { this.targetPublicRef = targetPublicRef; }

    public String getFinalFirstName() { return finalFirstName; }
    public void setFinalFirstName(String finalFirstName) { this.finalFirstName = finalFirstName; }

    public String getFinalLastName() { return finalLastName; }
    public void setFinalLastName(String finalLastName) { this.finalLastName = finalLastName; }

    public String getFinalEmail() { return finalEmail; }
    public void setFinalEmail(String finalEmail) { this.finalEmail = finalEmail; }

    public boolean isFinalActive() { return finalActive; }
    public void setFinalActive(boolean finalActive) { this.finalActive = finalActive; }

    public List<String> getActions() { return actions; }
    public void setActions(List<String> actions) { this.actions = actions; }
}
