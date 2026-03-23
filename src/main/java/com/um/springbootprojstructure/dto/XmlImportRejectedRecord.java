package com.um.springbootprojstructure.dto;

public class XmlImportRejectedRecord {
    private int index;
    private String email;
    private String reason;

    public XmlImportRejectedRecord() {}

    public XmlImportRejectedRecord(int index, String email, String reason) {
        this.index = index;
        this.email = email;
        this.reason = reason;
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
