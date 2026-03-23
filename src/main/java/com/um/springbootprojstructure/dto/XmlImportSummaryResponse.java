package com.um.springbootprojstructure.dto;

import java.util.ArrayList;
import java.util.List;

public class XmlImportSummaryResponse {

    private int totalRecords;
    private int importedCount;
    private int skippedCount;
    private int rejectedCount;

    private List<String> importedPublicRefs = new ArrayList<>();
    private List<String> skippedEmails = new ArrayList<>();
    private List<XmlImportRejectedRecord> rejected = new ArrayList<>();

    public XmlImportSummaryResponse() {}

    public int getTotalRecords() { return totalRecords; }
    public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }

    public int getImportedCount() { return importedCount; }
    public void setImportedCount(int importedCount) { this.importedCount = importedCount; }

    public int getSkippedCount() { return skippedCount; }
    public void setSkippedCount(int skippedCount) { this.skippedCount = skippedCount; }

    public int getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(int rejectedCount) { this.rejectedCount = rejectedCount; }

    public List<String> getImportedPublicRefs() { return importedPublicRefs; }
    public void setImportedPublicRefs(List<String> importedPublicRefs) { this.importedPublicRefs = importedPublicRefs; }

    public List<String> getSkippedEmails() { return skippedEmails; }
    public void setSkippedEmails(List<String> skippedEmails) { this.skippedEmails = skippedEmails; }

    public List<XmlImportRejectedRecord> getRejected() { return rejected; }
    public void setRejected(List<XmlImportRejectedRecord> rejected) { this.rejected = rejected; }
}
