package com.overops.plugins.runner;

import com.overops.plugins.Constants;
import com.overops.plugins.Result;
import com.overops.plugins.Util;
import com.overops.plugins.service.OverOpsService;
import com.overops.plugins.service.TeamCityPrintWriter;
import com.overops.report.service.QualityReportParams;
import com.overops.report.service.ReportService;
import com.overops.report.service.model.HtmlParts;
import com.overops.report.service.model.QualityReport;
import com.overops.report.service.model.QualityReport.ReportStatus;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.Callable;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import static com.overops.plugins.Constants.*;

public class OverOpsProcess implements Callable<BuildFinishedStatus> {

    @NotNull
    private final AgentRunningBuild agentRunningBuild;
    @NotNull
    private final BuildRunnerContext context;
    @NotNull
    private final BuildProgressLogger logger;
    @NotNull
    private ArtifactsWatcher artifactsWatcher;
    @NotNull
    private final OverOpsService overOpsService;

    public OverOpsProcess(@NotNull AgentRunningBuild agentRunningBuild, @NotNull BuildProgressLogger logger,
        @NotNull ArtifactsWatcher artifactsWatcher, @NotNull BuildRunnerContext context,
        @NotNull OverOpsService overOpsService) {
        this.agentRunningBuild = agentRunningBuild;
        this.logger = logger;
        this.artifactsWatcher = artifactsWatcher;
        this.context = context;
        this.overOpsService = overOpsService;
    }

    @Override
    public BuildFinishedStatus call() throws Exception {
        boolean createLink = Boolean.parseBoolean(context.getRunnerParameters().getOrDefault(FIELD_LINK, DEFAULT_LINK));
        boolean debug = Boolean.parseBoolean(context.getRunnerParameters().getOrDefault("debug", "false"));
        PrintStream printStream = debug ? new TeamCityPrintWriter(System.out, logger) : null;
        QualityReportParams reportParams = getQualityReportParams(context.getRunnerParameters());

        HtmlParts htmlParts = null;
        ReportService reportService = new ReportService();
        if (createLink)
        {
            String appUrl = context.getRunnerParameters().getOrDefault(SETTING_APP_URL, DEFAULT_APP_URL);
            String reportLinkHtml = reportService.generateReportLinkHtml(appUrl, reportParams, printStream, debug);
            htmlParts = new HtmlParts(reportLinkHtml, "");
            publishReportArtifact(htmlParts);
            publishArtifacts(false);
        } else
        {
            QualityReport reportModel = overOpsService.perform(reportService, reportParams, context, printStream);
            logger.message(reportModel.getStatusMsg());
            htmlParts = reportModel.getHtmlParts(reportParams.isShowEventsForPassedGates());
            publishReportArtifact(htmlParts);
            publishArtifacts((reportModel.getStatusCode() == ReportStatus.FAILED) || (reportModel.getStatusCode() == ReportStatus.WARNING));

            if (reportModel.getStatusCode() == ReportStatus.FAILED) {
                if ((reportModel.getExceptionDetails() == null) || !Boolean.parseBoolean(context.getRunnerParameters().getOrDefault("errorSuccess", "false"))) {
                    return BuildFinishedStatus.FINISHED_FAILED;
                }
            }
        }


        return BuildFinishedStatus.FINISHED_SUCCESS;
    }

    private void publishArtifacts(Boolean markUnstable) {
        File buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" +
            agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" +
            agentRunningBuild.getBuildNumber() + "/" + RUNNER_DISPLAY_NAME);
        File file = new File(buildDirectory, OV_REPORTS_FILE);
        try {
            FileUtils.touch(file);
            Result result = new Result(markUnstable);
            Util.objectToString(result).ifPresent(o -> appendStringToFile(file, o));
        } catch (IOException e) {
            logger.error("Cannot create artifact: " + e.getMessage());
        }
        artifactsWatcher.addNewArtifactsPath(file + "=>" + RUNNER_DISPLAY_NAME);
    }

    private void publishReportArtifact(HtmlParts htmlParts) {
        File buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" +
                agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" +
                agentRunningBuild.getBuildNumber() + "/" + RUNNER_DISPLAY_NAME);
        File file = new File(buildDirectory, OV_REPORTS_FILE_RESULT);
        try {
            FileUtils.touch(file);
            Util.objectToString(htmlParts).ifPresent(o -> appendStringToFile(file, o));
        } catch (IOException e) {
            logger.error("Cannot create result artifact: " + e.getMessage());
        }
        artifactsWatcher.addNewArtifactsPath(file + "=>" + RUNNER_DISPLAY_NAME);
    }

    private void appendStringToFile(File file, String content) {
        try {
            Files.write(Paths.get(file.toURI()), content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Cannot write line into artifact: " + e.getMessage());
        }
    }

    private QualityReportParams getQualityReportParams(Map<String, String> params) {
        QualityReportParams queryOverOps = new QualityReportParams();
        queryOverOps.setApplicationName(params.get(Constants.APP_NAME));
        queryOverOps.setDeploymentName(params.get(Constants.DEPLOYMENT_NAME));
        queryOverOps.setServiceId(params.get(Constants.SETTING_ENV_ID));
        queryOverOps.setRegexFilter(params.getOrDefault("regexFilter", ""));
        queryOverOps.setMarkUnstable(Boolean.parseBoolean(params.getOrDefault(Constants.FIELD_MARK_UNSTABLE, DEFAULT_MARK_UNSTABLE)));
        queryOverOps.setPrintTopIssues(Integer.parseInt(params.getOrDefault(Constants.FIELD_PRINT_TOP_ISSUE, "5")));
        queryOverOps.setShowEventsForPassedGates(Boolean.parseBoolean(params.getOrDefault(FIELD_SHOW_PASSED_GATE_EVENTS, "false")));
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

}
