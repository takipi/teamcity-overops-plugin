package com.overops.plugins.model;

public class ReportRegressedModel extends ReportEventModel {
    private long baselineHits;
    private long baselineInvocations;
    private String eventRate;

    public long getBaselineHits() {
        return baselineHits;
    }

    public void setBaselineHits(long baselineHits) {
        this.baselineHits = baselineHits;
    }

    public long getBaselineInvocations() {
        return baselineInvocations;
    }

    public void setBaselineInvocations(long baselineInvocations) {
        this.baselineInvocations = baselineInvocations;
    }

    public String getEventRate() {
        return eventRate;
    }

    public void setEventRate(String eventRate) {
        this.eventRate = eventRate;
    }
}
