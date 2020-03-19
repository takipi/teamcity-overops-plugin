package com.overops.plugins.model;

import com.takipi.api.client.util.cicd.OOReportEvent;

public class ReportEventModel {
    private final String arcLink;
    private final String type;
    private final String applications;
    private final String introducedBy;
    private final String eventSummary;
    private final String eventRate;
    private final long hits;
    private final long calls;

    public ReportEventModel(OOReportEvent e) {
        this.arcLink = e.getARCLink();
        this.type= e.getType();
        this.introducedBy = e.getIntroducedBy();
        this.eventSummary  = e.getEventSummary();
        this.eventRate = e.getEventRate();
        this.hits = e.getHits();
        this.calls = e.getCalls();
        this.applications = e.getApplications();
    }

    public String getArcLink() {
        return arcLink;
    }

    public String getType() {
        return type;
    }

    public String getApplications() {
        return applications;
    }

    public String getIntroducedBy() {
        return introducedBy;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public String getEventRate() {
        return eventRate;
    }

    public long getHits() {
        return hits;
    }

    public long getCalls() {
        return calls;
    }
}
