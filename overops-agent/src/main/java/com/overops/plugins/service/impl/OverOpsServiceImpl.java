package com.overops.plugins.service.impl;

import com.overops.plugins.Constants;
import com.overops.plugins.service.OverOpsService;
import com.overops.report.service.QualityReportParams;
import com.overops.report.service.ReportService;
import com.overops.report.service.ReportService.Requestor;
import com.overops.report.service.model.QualityReport;
import com.overops.report.service.model.QualityReportExceptionDetails;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;

import org.springframework.util.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OverOpsServiceImpl implements OverOpsService {

    @Override
    public QualityReport perform(BuildRunnerContext context, BuildProgressLogger logger) {
        String endPoint = context.getRunnerParameters().get(Constants.SETTING_URL);
        String apiKey = context.getRunnerParameters().get(Constants.SETTING_TOKEN);
        boolean debug = Boolean.parseBoolean(context.getRunnerParameters().getOrDefault("debug", "false"));
        PrintStream printStream = debug ? new TeamCityPrintWriter(System.out, logger) : null;    

        QualityReportParams query = getQualityReportParams(context.getRunnerParameters());

        QualityReport reportModel = null;
        ReportService reportService = new ReportService();
        try {
            validateInputs(endPoint, apiKey, query, printStream);
            reportModel = reportService.runQualityReport(endPoint, apiKey, query, Requestor.TEAM_CITY, printStream, debug);
        } catch (Exception exception) {
            reportModel = new QualityReport();

            QualityReportExceptionDetails exceptionDetails = new QualityReportExceptionDetails();
            exceptionDetails.setExceptionMessage(exception.getMessage());

            List<StackTraceElement> stackElements = Arrays.asList(exception.getStackTrace());
            List<String> stackTrace = new ArrayList<>();
            stackTrace.add(exception.getClass().getName());
            stackTrace.addAll(stackElements.stream().map(stack -> stack.toString()).collect(Collectors.toList()));
            exceptionDetails.setStackTrace(stackTrace.toArray(new String[stackTrace.size()]));

            reportModel.setExceptionDetails(exceptionDetails);
        }

        return reportModel;

    }

    private QualityReportParams getQualityReportParams(Map<String, String> params) {
        QualityReportParams queryOverOps = new QualityReportParams();
        queryOverOps.setApplicationName(params.get(Constants.APP_NAME));
        queryOverOps.setDeploymentName(params.get(Constants.DEPLOYMENT_NAME));
        queryOverOps.setServiceId(params.get(Constants.SETTING_ENV_ID));
        queryOverOps.setRegexFilter(params.getOrDefault("regexFilter", ""));
        queryOverOps.setMarkUnstable(Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_MARK_UNSTABLE, "false")));
        queryOverOps.setPrintTopIssues(Integer.parseInt(params.getOrDefault(Constants.FIELD_PRINT_TOP_ISSUE, "5")));
        queryOverOps.setNewEvents(Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_CHECK_NEW_ERROR, "false")));
        queryOverOps.setResurfacedErrors(Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_CHECK_RESURFACED_ERRORS, "false")));
        if (Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_VOLUME_ERRORS, "false"))) {
            queryOverOps.setMaxErrorVolume(Integer.parseInt(params.getOrDefault(Constants.FIELD_MAX_ERROR_VOLUME, "1")));
        } else {
            queryOverOps.setMaxErrorVolume(0);
        }
        if (Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_UNIQUE_ERRORS, "false"))) {
            queryOverOps.setMaxUniqueErrors(Integer.parseInt(params.getOrDefault(Constants.FIELD_MAX_UNIQUE_ERRORS, "1")));
        } else {
            queryOverOps.setMaxUniqueErrors(0);
        }
        if (Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_CRITICAL_ERRORS, "false"))) {
            queryOverOps.setCriticalExceptionTypes(params.getOrDefault("criticalExceptionTypes", ""));
        } else {
            queryOverOps.setCriticalExceptionTypes("");
        }

        queryOverOps.setActiveTimespan("0");
        queryOverOps.setBaselineTimespan("0");
        queryOverOps.setMinVolumeThreshold(0);
        queryOverOps.setMinErrorRateThreshold(0);
        queryOverOps.setRegressionDelta(0);
        queryOverOps.setCriticalRegressionDelta(0);
        queryOverOps.setApplySeasonality(false);

        return queryOverOps;
    }

    //sleep to ensure all data is in OverOps
    private static void pauseForTheCause(PrintStream printStream) {
        if (Objects.nonNull(printStream)) {
            printStream.println("Build Step: Starting OverOps Quality Gate....");
        }
        try {
            Thread.sleep(30000);
        } catch (Exception e) {
            if (Objects.nonNull(printStream)) {
                printStream.println("Can not hold the process.");
            }
        }
    }

    private void validateInputs (String apiHost, String apiKey, QualityReportParams queryOverOps, PrintStream printStream) {
        if (StringUtils.isEmpty(apiHost)) {
            throw new IllegalArgumentException("Missing host name");
        }

        if (StringUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("Missing api key");
        }

        if (!"0".equalsIgnoreCase(queryOverOps.getActiveTimespan())) {
            if (convertToMinutes(queryOverOps.getActiveTimespan()) == 0) {
                if (printStream != null) {
                    printStream.println("For Increasing Error Gate, the active timewindow currently set to: " + queryOverOps.getActiveTimespan() +  " is not properly formated. See help for format instructions.");
                }
                throw new IllegalArgumentException("For Increasing Error Gate, the active timewindow currently set to: " + queryOverOps.getActiveTimespan() +  " is not properly formated. See help for format instructions.");
            }
        }
        if (!"0".equalsIgnoreCase(queryOverOps.getBaselineTimespan())) {
            if (convertToMinutes(queryOverOps.getBaselineTimespan()) == 0) {
                if (printStream != null) {
                    printStream.println("For Increasing Error Gate, the baseline timewindow currently set to: " + queryOverOps.getBaselineTimespan() + " cannot be zero or is improperly formated. See help for format instructions.");
                }
                throw new IllegalArgumentException("For Increasing Error Gate, the baseline timewindow currently set to: " + queryOverOps.getBaselineTimespan() + " cannot be zero or is improperly formated. See help for format instructions.");
            }
        }

        if (StringUtils.isEmpty(queryOverOps.getServiceId())) {
            throw new IllegalArgumentException("Missing environment Id");
        }
    }

    private int convertToMinutes(String timeWindow) {

        if (StringUtils.isEmpty(timeWindow)) {
          return 0;
        }

        if (timeWindow.toLowerCase().contains("d")) {
            int days = Integer.parseInt(timeWindow.substring(0, timeWindow.indexOf("d")));
            return days * 24 * 60;
        } else if (timeWindow.toLowerCase().contains("h")) {
            int hours = Integer.parseInt(timeWindow.substring(0, timeWindow.indexOf("h")));
            return hours * 60;
        } else if (timeWindow.toLowerCase().contains("m")) {
            return Integer.parseInt(timeWindow.substring(0, timeWindow.indexOf("m")));
        }

        return 0;
    }
}
