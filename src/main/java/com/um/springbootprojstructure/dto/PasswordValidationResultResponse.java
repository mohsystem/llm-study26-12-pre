package com.um.springbootprojstructure.dto;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidationResultResponse {

    public enum Status { ACCEPTED, REJECTED }

    private Status status;
    private List<String> codes = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public PasswordValidationResultResponse() {}

    public PasswordValidationResultResponse(Status status) {
        this.status = status;
    }

    public static PasswordValidationResultResponse accepted() {
        return new PasswordValidationResultResponse(Status.ACCEPTED);
    }

    public static PasswordValidationResultResponse rejected(List<String> codes, List<String> messages) {
        PasswordValidationResultResponse r = new PasswordValidationResultResponse(Status.REJECTED);
        r.setCodes(codes);
        r.setMessages(messages);
        return r;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<String> getCodes() { return codes; }
    public void setCodes(List<String> codes) { this.codes = codes; }

    public List<String> getMessages() { return messages; }
    public void setMessages(List<String> messages) { this.messages = messages; }
}
