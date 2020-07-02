package com.overops.plugins.service;

import com.overops.plugins.Constants;
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
import java.util.stream.Collectors;

public class OverOpsService
{
    public QualityReport perform(ReportService reportService, QualityReportParams reportParams, BuildRunnerContext context, PrintStream printStream) {
        String endPoint = context.getRunnerParameters().get(Constants.SETTING_URL);
        String apiKey = context.getRunnerParameters().get(Constants.SETTING_TOKEN);
        boolean debug = Boolean.parseBoolean(context.getRunnerParameters().getOrDefault("debug", "false"));

        QualityReport reportModel = null;
        try {
            validateInputs(endPoint, apiKey, reportParams, printStream);
            reportService.pauseForTheCause(printStream);
            reportModel = reportService.runQualityReport(endPoint, apiKey, reportParams, Requestor.TEAM_CITY, printStream, debug);
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
