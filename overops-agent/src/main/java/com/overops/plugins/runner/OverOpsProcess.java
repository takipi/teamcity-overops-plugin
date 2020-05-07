package com.overops.plugins.runner;

import com.overops.plugins.Result;
import com.overops.plugins.Util;
import com.overops.plugins.service.OverOpsService;
import com.overops.report.service.model.QualityReport;
import com.overops.report.service.model.QualityReport.ReportStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        QualityReport reportModel = overOpsService.perform(context, logger);

        publishReportArtifact(reportModel);

        logger.message(reportModel.getStatusMsg());

        if (reportModel.getExceptionDetails() != null) {
            return Boolean.parseBoolean(context.getRunnerParameters().getOrDefault("errorSuccess", "false")) ? BuildFinishedStatus.FINISHED_SUCCESS : BuildFinishedStatus.INTERRUPTED;
        } else {
            publishArtifacts((reportModel.getStatusCode() == ReportStatus.FAILED) || (reportModel.getStatusCode() == ReportStatus.WARNING));
            if (reportModel.getStatusCode() == ReportStatus.FAILED) {
                return BuildFinishedStatus.FINISHED_FAILED;
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

    private void publishReportArtifact(QualityReport report) {
        File buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" +
                agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" +
                agentRunningBuild.getBuildNumber() + "/" + RUNNER_DISPLAY_NAME);
        File file = new File(buildDirectory, OV_REPORTS_FILE_RESULT);
        try {
            FileUtils.touch(file);
            Util.objectToString(report.getHtmlParts()).ifPresent(o -> appendStringToFile(file, o));
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

}
