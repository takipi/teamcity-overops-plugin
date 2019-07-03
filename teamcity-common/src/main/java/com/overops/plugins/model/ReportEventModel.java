package com.overops.plugins.model;

public class ReportEventModel {
    private String arcLink;
    private String type;
    private String applications;
    private String introducedBy;
    private String eventSummary;
    private String eventRate;
    private long hits;
    private long calls;

    public ReportEventModel() {
    }

    public ReportEventModel(String arcLink, String type, String introducedBy) {
        this.arcLink = arcLink;
        this.type = type;
        this.introducedBy = introducedBy;
    }

    public ReportEventModel(String arcLink, String type, String introducedBy, String eventSummary, String eventRate, long hits, long calls, String applications) {
        this.arcLink = arcLink;
        this.type = type;
        this.introducedBy = introducedBy;
        this.eventSummary = eventSummary;
        this.eventRate = eventRate;
        this.hits = hits;
        this.calls = calls;
        this.applications = applications;
    }

    public String getArcLink() {
        return arcLink;
    }

    public void setArcLink(String arcLink) {
        this.arcLink = arcLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApplications() {
        return applications;
    }

    public void setApplications(String applications) {
        this.applications = applications;
    }

    public String getIntroducedBy() {
        return introducedBy;
    }

    public void setIntroducedBy(String introducedBy) {
        this.introducedBy = introducedBy;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public void setEventSummary(String eventSummary) {
        this.eventSummary = eventSummary;
    }

    public String getEventRate() {
        return eventRate;
    }

    public void setEventRate(String eventRate) {
        this.eventRate = eventRate;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public long getCalls() {
        return calls;
    }

    public void setCalls(long calls) {
        this.calls = calls;
    }
}
