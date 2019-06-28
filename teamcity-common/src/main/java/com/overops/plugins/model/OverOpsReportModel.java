package com.overops.plugins.model;

import java.util.List;

public class OverOpsReportModel {
    private boolean unstable;
    private boolean markedUnstable;
    private boolean passedNewErrorGate;
    private boolean checkNewEvents;
    private boolean passedResurfacedErrorGate;
    private boolean checkResurfacedEvents;
    private boolean checkCriticalErrors;
    private boolean passedCriticalErrorGate;
    private boolean checkTotalErrors;
    private boolean passedTotalErrorGate;
    private boolean checkUniqueErrors;
    private boolean hasTopErrors;
    private boolean passedUniqueErrorGate;
    private boolean checkRegressedErrors;
    private boolean passedRegressedEvents;

    private String summary;
    private String newErrorSummary;
    private String resurfacedErrorSummary;
    private String criticalErrorSummary;
    private String totalErrorSummary;
    private String uniqueErrorSummary;
    private String regressionSumarry;

    private List<ReportEventModel> newEvents;
    private List<ReportEventModel> resurfacedEvents;
    private List<ReportEventModel> criticalEvents;
    private List<ReportEventModel> topEvents;
    private List<ReportEventModel> regressedEvents;

    public boolean isUnstable() {
        return unstable;
    }

    public void setUnstable(boolean unstable) {
        this.unstable = unstable;
    }

    public boolean isMarkedUnstable() {
        return markedUnstable;
    }

    public void setMarkedUnstable(boolean markedUnstable) {
        this.markedUnstable = markedUnstable;
    }

    public boolean isPassedNewErrorGate() {
        return passedNewErrorGate;
    }

    public void setPassedNewErrorGate(boolean passedNewErrorGate) {
        this.passedNewErrorGate = passedNewErrorGate;
    }

    public boolean isCheckNewEvents() {
        return checkNewEvents;
    }

    public void setCheckNewEvents(boolean checkNewEvents) {
        this.checkNewEvents = checkNewEvents;
    }

    public boolean isPassedResurfacedErrorGate() {
        return passedResurfacedErrorGate;
    }

    public void setPassedResurfacedErrorGate(boolean passedResurfacedErrorGate) {
        this.passedResurfacedErrorGate = passedResurfacedErrorGate;
    }

    public boolean isCheckResurfacedEvents() {
        return checkResurfacedEvents;
    }

    public void setCheckResurfacedEvents(boolean checkResurfacedEvents) {
        this.checkResurfacedEvents = checkResurfacedEvents;
    }

    public boolean isCheckCriticalErrors() {
        return checkCriticalErrors;
    }

    public void setCheckCriticalErrors(boolean checkCriticalErrors) {
        this.checkCriticalErrors = checkCriticalErrors;
    }

    public boolean isPassedCriticalErrorGate() {
        return passedCriticalErrorGate;
    }

    public void setPassedCriticalErrorGate(boolean passedCriticalErrorGate) {
        this.passedCriticalErrorGate = passedCriticalErrorGate;
    }

    public boolean isCheckTotalErrors() {
        return checkTotalErrors;
    }

    public void setCheckTotalErrors(boolean checkTotalErrors) {
        this.checkTotalErrors = checkTotalErrors;
    }

    public boolean isPassedTotalErrorGate() {
        return passedTotalErrorGate;
    }

    public void setPassedTotalErrorGate(boolean passedTotalErrorGate) {
        this.passedTotalErrorGate = passedTotalErrorGate;
    }

    public boolean isCheckUniqueErrors() {
        return checkUniqueErrors;
    }

    public void setCheckUniqueErrors(boolean checkUniqueErrors) {
        this.checkUniqueErrors = checkUniqueErrors;
    }

    public boolean isHasTopErrors() {
        return hasTopErrors;
    }

    public void setHasTopErrors(boolean hasTopErrors) {
        this.hasTopErrors = hasTopErrors;
    }

    public boolean isPassedUniqueErrorGate() {
        return passedUniqueErrorGate;
    }

    public void setPassedUniqueErrorGate(boolean passedUniqueErrorGate) {
        this.passedUniqueErrorGate = passedUniqueErrorGate;
    }

    public boolean isCheckRegressedErrors() {
        return checkRegressedErrors;
    }

    public void setCheckRegressedErrors(boolean checkRegressedErrors) {
        this.checkRegressedErrors = checkRegressedErrors;
    }

    public boolean isPassedRegressedEvents() {
        return passedRegressedEvents;
    }

    public void setPassedRegressedEvents(boolean passedRegressedEvents) {
        this.passedRegressedEvents = passedRegressedEvents;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNewErrorSummary() {
        return newErrorSummary;
    }

    public void setNewErrorSummary(String newErrorSummary) {
        this.newErrorSummary = newErrorSummary;
    }

    public String getTotalErrorSummary() {
        return totalErrorSummary;
    }

    public void setTotalErrorSummary(String totalErrorSummary) {
        this.totalErrorSummary = totalErrorSummary;
    }

    public List<ReportEventModel> getNewEvents() {
        return newEvents;
    }

    public void setNewEvents(List<ReportEventModel> newEvents) {
        this.newEvents = newEvents;
    }

    public String getResurfacedErrorSummary() {
        return resurfacedErrorSummary;
    }

    public void setResurfacedErrorSummary(String resurfacedErrorSummary) {
        this.resurfacedErrorSummary = resurfacedErrorSummary;
    }

    public String getUniqueErrorSummary() {
        return uniqueErrorSummary;
    }

    public void setUniqueErrorSummary(String uniqueErrorSummary) {
        this.uniqueErrorSummary = uniqueErrorSummary;
    }

    public String getRegressionSumarry() {
        return regressionSumarry;
    }

    public void setRegressionSumarry(String regressionSumarry) {
        this.regressionSumarry = regressionSumarry;
    }

    public List<ReportEventModel> getResurfacedEvents() {
        return resurfacedEvents;
    }

    public void setResurfacedEvents(List<ReportEventModel> resurfacedEvents) {
        this.resurfacedEvents = resurfacedEvents;
    }

    public String getCriticalErrorSummary() {
        return criticalErrorSummary;
    }

    public void setCriticalErrorSummary(String criticalErrorSummary) {
        this.criticalErrorSummary = criticalErrorSummary;
    }

    public List<ReportEventModel> getCriticalEvents() {
        return criticalEvents;
    }

    public void setCriticalEvents(List<ReportEventModel> criticalEvents) {
        this.criticalEvents = criticalEvents;
    }

    public List<ReportEventModel> getTopEvents() {
        return topEvents;
    }

    public void setTopEvents(List<ReportEventModel> topEvents) {
        this.topEvents = topEvents;
    }

    public List<ReportEventModel> getRegressedEvents() {
        return regressedEvents;
    }

    public void setRegressedEvents(List<ReportEventModel> regressedEvents) {
        this.regressedEvents = regressedEvents;
    }
}
