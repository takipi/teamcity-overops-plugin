package com.overops.plugins.model;

import org.springframework.util.StringUtils;

import java.io.PrintStream;
import java.util.Map;

import static com.overops.plugins.Constants.*;
import static com.overops.plugins.Util.convertToMinutes;

public class OverOpsConfiguration {
    private final String appHost;
    private final String environmentID;
    private final String apiKey;
    private final String applicationName;
    private final String deploymentName;
    private final String serviceId;
    private final String regexFilter;
    private final boolean markUnstable;
    private final Integer printTopIssues;
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
    private final boolean debug;
    private final boolean errorSuccess;

    public OverOpsConfiguration(Map<String, String> params) {
        this.appHost = params.get(SETTING_APP_HOST);
        this.environmentID = params.get(SETTING_ENV_ID).toUpperCase();
        this.apiKey = params.get(SETTING_API_TOKEN);
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

    public String getApplicationName() {
        return applicationName;
    }

    public String getDeploymentName() {
        return deploymentName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getRegexFilter() {
        return regexFilter;
    }

    public boolean isMarkUnstable() {
        return markUnstable;
    }

    public Integer getPrintTopIssues() {
        return printTopIssues;
    }

    public boolean isNewEvents() {
        return newEvents;
    }

    public boolean isResurfacedErrors() {
        return resurfacedErrors;
    }

    public Integer getMaxErrorVolume() {
        return maxErrorVolume;
    }

    public boolean isMaxErrorVolume() {
        return getMaxErrorVolume() != 0;
    }

    public boolean checkIfMaxVolumeExceeded(long totalErrorsCount) {
        return isMaxErrorVolume() && totalErrorsCount > getMaxErrorVolume();
    }

    public Integer getMaxUniqueErrors() {
        return maxUniqueErrors;
    }

    public boolean isMaxUniqueErrors() {
        return getMaxUniqueErrors() != 0;
    }

    public boolean checkIfMaxUniqueErrorsExceeded(long uniqueErrorCount) {
        return isMaxUniqueErrors() && uniqueErrorCount > getMaxUniqueErrors();
    }

    public String getCriticalExceptionTypes() {
        return criticalExceptionTypes;
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

    public String getBaselineTimespan() {
        return baselineTimespan;
    }

    public int getBaselineTimespanMinutes() {
        return convertToMinutes(baselineTimespan);
    }

    public boolean isRegressionPresent() {
        return getBaselineTimespanMinutes() > 0;
    }

    public Integer getMinVolumeThreshold() {
        return minVolumeThreshold;
    }

    public Double getMinErrorRateThreshold() {
        return minErrorRateThreshold;
    }

    public Double getRegressionDelta() {
        return regressionDelta;
    }

    public Double getCriticalRegressionDelta() {
        return criticalRegressionDelta;
    }

    public boolean isApplySeasonality() {
        return applySeasonality;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isErrorSuccess() {
        return errorSuccess;
    }

    public String getAppHost() {
        return appHost;
    }

    public String getEnvironmentID() {
        return environmentID;
    }

    public String getAPIKey() {
        return apiKey;
    }

    public boolean isCountGatePresent() {
        return getMaxErrorVolume() != 0 || getMaxUniqueErrors() != 0;
    }

    public boolean isSomeGateBesideRegressionToProcess() {
        return isCountGatePresent() || isNewEvents() || isResurfacedErrors() || getRegexFilter() != null;
    }

    public void validate(PrintStream printStream) {
        if (StringUtils.isEmpty(appHost)) {
            throw new IllegalArgumentException("Missing host name");
        }

        if (StringUtils.isEmpty(environmentID)) {
            throw new IllegalArgumentException("Missing environment Id");
        }

        if (StringUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("Missing api key");
        }

        if (getCheckRegressionErrors()) {
            if (!"0".equalsIgnoreCase(getActiveTimespan())) {
                if (convertToMinutes(getActiveTimespan()) == 0) {
                    printStream.println("For Increasing Error Gate, the active timewindow currently set to: " + getActiveTimespan() + " is not properly formated. See help for format instructions.");
                    throw new IllegalArgumentException("For Increasing Error Gate, the active timewindow currently set to: " + getActiveTimespan() + " is not properly formated. See help for format instructions.");
                }
            }
            if (!"0".equalsIgnoreCase(getBaselineTimespan())) {
                if (convertToMinutes(getBaselineTimespan()) == 0) {
                    printStream.println("For Increasing Error Gate, the baseline timewindow currently set to: " + getBaselineTimespan() + " cannot be zero or is improperly formated. See help for format instructions.");
                    throw new IllegalArgumentException("For Increasing Error Gate, the baseline timewindow currently set to: " + getBaselineTimespan() + " cannot be zero or is improperly formated. See help for format instructions.");
                }
            }
        }
    }

    @Override
    public String toString() {
        return "QueryOverOps{" +
                "overOpsURL='" + appHost + '\'' +
                ", overOpsSID='" + environmentID + '\'' +
                ", overOpsAPIKey='" + apiKey + '\'' +
                ", applicationName='" + applicationName + '\'' +
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
