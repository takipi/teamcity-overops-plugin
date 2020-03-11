package com.overops.plugins.model;

import java.util.Map;

public class QueryOverOps {
    private String applicationName;
    private String deploymentName;
    private String serviceId;
    private String regexFilter;
    private boolean markUnstable;
    private Integer printTopIssues;
    private boolean newEvents = false;
    private boolean resurfacedErrors = false;
    private Integer maxErrorVolume = 0;
    private Integer maxUniqueErrors = 0;
    private String criticalExceptionTypes;
    private boolean checkRegressionErrors;
    private String activeTimespan = "0";
    private String baselineTimespan = "0";
    private Integer minVolumeThreshold = 0;
    private Double minErrorRateThreshold = 0d;
    private Double regressionDelta = 0d;
    private Double criticalRegressionDelta = 0d;
    private boolean applySeasonality = false;

    // advanced settings
    private boolean debug;
    private boolean errorSuccess;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public void setDeploymentName(String deploymentName) {
        this.deploymentName = deploymentName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRegexFilter() {
        return regexFilter;
    }

    public void setRegexFilter(String regexFilter) {
        this.regexFilter = regexFilter;
    }

    public boolean isMarkUnstable() {
        return markUnstable;
    }

    public void setMarkUnstable(boolean markUnstable) {
        this.markUnstable = markUnstable;
    }

    public Integer getPrintTopIssues() {
        return printTopIssues;
    }

    public void setPrintTopIssues(Integer printTopIssues) {
        this.printTopIssues = printTopIssues;
    }

    public boolean isNewEvents() {
        return newEvents;
    }

    public void setNewEvents(boolean newEvents) {
        this.newEvents = newEvents;
    }

    public boolean isResurfacedErrors() {
        return resurfacedErrors;
    }

    public void setResurfacedErrors(boolean resurfacedErrors) {
        this.resurfacedErrors = resurfacedErrors;
    }

    public Integer getMaxErrorVolume() {
        return maxErrorVolume;
    }

    public void setMaxErrorVolume(Integer maxErrorVolume) {
        this.maxErrorVolume = maxErrorVolume;
    }

    public Integer getMaxUniqueErrors() {
        return maxUniqueErrors;
    }

    public void setMaxUniqueErrors(Integer maxUniqueErrors) {
        this.maxUniqueErrors = maxUniqueErrors;
    }

    public String getCriticalExceptionTypes() {
        return criticalExceptionTypes;
    }

    public void setCriticalExceptionTypes(String criticalExceptionTypes) {
        this.criticalExceptionTypes = criticalExceptionTypes;
    }

    public Boolean getCheckRegressionErrors() {
        return checkRegressionErrors;
    }

    private void setCheckRegressionErrors(Boolean checkRegressionErrors) {
        this.checkRegressionErrors = checkRegressionErrors;
    }

    public String getActiveTimespan() {
        return activeTimespan;
    }

    public void setActiveTimespan(String activeTimespan) {
        this.activeTimespan = activeTimespan;
    }

    public String getBaselineTimespan() {
        return baselineTimespan;
    }

    public void setBaselineTimespan(String baselineTimespan) {
        this.baselineTimespan = baselineTimespan;
    }

    public Integer getMinVolumeThreshold() {
        return minVolumeThreshold;
    }

    public void setMinVolumeThreshold(Integer minVolumeThreshold) {
        this.minVolumeThreshold = minVolumeThreshold;
    }

    public Double getMinErrorRateThreshold() {
        return minErrorRateThreshold;
    }

    public void setMinErrorRateThreshold(Double minErrorRateThreshold) {
        this.minErrorRateThreshold = minErrorRateThreshold;
    }

    public Double getRegressionDelta() {
        return regressionDelta;
    }

    public void setRegressionDelta(Double regressionDelta) {
        this.regressionDelta = regressionDelta;
    }

    public Double getCriticalRegressionDelta() {
        return criticalRegressionDelta;
    }

    public void setCriticalRegressionDelta(Double criticalRegressionDelta) {
        this.criticalRegressionDelta = criticalRegressionDelta;
    }

    public boolean isApplySeasonality() {
        return applySeasonality;
    }

    public void setApplySeasonality(boolean applySeasonality) {
        this.applySeasonality = applySeasonality;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isErrorSuccess() {
        return errorSuccess;
    }

    public void setErrorSuccess(boolean errorSuccess) {
      this.errorSuccess = errorSuccess;
    }

    public QueryOverOps(Map<String, String> params) {
        applicationName = params.get("applicationName");
        deploymentName = params.get("deploymentName");
        serviceId = params.get("serviceId");
        regexFilter = params.getOrDefault("regexFilter", "");
        markUnstable = Boolean.parseBoolean(params.getOrDefault("markUnstable", "false"));
        printTopIssues = Integer.parseInt(params.getOrDefault("printTopIssues", "5"));
        if (Boolean.parseBoolean(params.getOrDefault("checkNewErrors", "false"))) {
            newEvents = Boolean.parseBoolean(params.getOrDefault("newEvents", "false"));
        }
        if (Boolean.parseBoolean(params.getOrDefault("checkResurfacedErrors", "false"))) {
            resurfacedErrors = Boolean.parseBoolean(params.getOrDefault("resurfacedErrors", "false"));
        }
        if (Boolean.parseBoolean(params.getOrDefault("checkVolumeErrors", "false"))) {
            maxErrorVolume = Integer.parseInt(params.getOrDefault("maxErrorVolume", "0"));
        }
        if (Boolean.parseBoolean(params.getOrDefault("checkUniqueErrors", "false"))) {
            maxUniqueErrors = Integer.parseInt(params.getOrDefault("maxUniqueErrors", "0"));
        }
        if (Boolean.parseBoolean(params.getOrDefault("checkCriticalErrors", "false"))) {
            criticalExceptionTypes = params.getOrDefault("criticalExceptionTypes", "");
        }
        setCheckRegressionErrors(Boolean.parseBoolean(params.getOrDefault("checkRegressionErrors", "false")));
        if (getCheckRegressionErrors()) {
            activeTimespan = params.getOrDefault("activeTimespan", "0");
            baselineTimespan = params.getOrDefault("baselineTimespan", "0");
            minVolumeThreshold = Integer.parseInt(params.getOrDefault("minVolumeThreshold", "0"));
            minErrorRateThreshold = Double.parseDouble(params.getOrDefault("minErrorRateThreshold", "0"));
            regressionDelta = Double.parseDouble(params.getOrDefault("regressionDelta", "0"));
            criticalRegressionDelta = Double.parseDouble(params.getOrDefault("criticalRegressionDelta", "0"));
            applySeasonality = Boolean.parseBoolean(params.getOrDefault("applySeasonality", "false"));
        }
        debug = Boolean.parseBoolean(params.getOrDefault("debug", "false"));
        errorSuccess = Boolean.parseBoolean(params.getOrDefault("errorSuccess", "false"));
    }

    @Override
    public String toString() {
        return "QueryOverOps{" +
                "applicationName='" + applicationName + '\'' +
                ", deploymentName='" + deploymentName + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", regexFilter='" + regexFilter + '\'' +
                ", markUnstable=" + markUnstable +
                ", printTopIssues=" + printTopIssues +
                ", newEvents=" + newEvents +
                ", resurfacedErrors=" + resurfacedErrors +
                ", maxErrorVolume=" + maxErrorVolume +
                ", maxUniqueErrors=" + maxUniqueErrors +
                ", criticalExceptionTypes='" + criticalExceptionTypes + '\'' +
                ", activeTimespan='" + activeTimespan + '\'' +
                ", baselineTimespan='" + baselineTimespan + '\'' +
                ", minVolumeThreshold=" + minVolumeThreshold +
                ", minErrorRateThreshold=" + minErrorRateThreshold +
                ", regressionDelta=" + regressionDelta +
                ", criticalRegressionDelta=" + criticalRegressionDelta +
                ", applySeasonality=" + applySeasonality +
                ", debug=" + debug +
                ", errorSuccess=" + errorSuccess +
                '}';
    }
}
